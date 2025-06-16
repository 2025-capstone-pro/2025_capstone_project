from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
from peft import PeftModel, PeftConfig
import uvicorn
from contextlib import asynccontextmanager
import logging
import os
from pathlib import Path

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 전역 변수로 모델과 토크나이저 저장
model = None
tokenizer = None

# 요청/응답 모델 정의
class QuestionRequest(BaseModel):
    promptText: str
    max_length: int = 256
    temperature: float = 0.7
    top_p: float = 0.9

class AnswerResponse(BaseModel):
    ResponseText: str

class HealthResponse(BaseModel):
    status: str
    model_loaded: bool

# 모델 로딩 함수
def load_model():
    global model, tokenizer
    
    # 이미 모델이 로드되어 있다면 스킵
    if model is not None and tokenizer is not None:
        logger.info("모델이 이미 로드되어 있습니다.")
        return
    
    try:
        # 경로 설정 - 상대경로로 수정
        current_dir = Path(__file__).parent
        model_dir = current_dir / "lora"
        
        logger.info(f"모델 디렉토리: {model_dir}")
        
        # 디렉토리 존재 확인
        if not model_dir.exists():
            raise FileNotFoundError(f"모델 디렉토리를 찾을 수 없습니다: {model_dir}")

        logger.info("PEFT 구성 로드 중...")
        peft_config = PeftConfig.from_pretrained(model_dir)
        
        # HuggingFace 캐시 디렉토리 설정
        cache_dir = os.path.join(os.path.expanduser("~"), ".cache", "huggingface")
        os.makedirs(cache_dir, exist_ok=True)
        
        logger.info("Base 모델 로드 중...")
        base_model = AutoModelForCausalLM.from_pretrained(
            peft_config.base_model_name_or_path,
            return_dict=True,
            torch_dtype=torch.float16,
            device_map="auto",  # GPU 자동 할당
            cache_dir=cache_dir  # 캐시 디렉토리 지정
        )
        
        logger.info("LoRA weight 로드 중...")
        model = PeftModel.from_pretrained(base_model, model_dir)
        model.eval()
        
        logger.info("토크나이저 로드 중...")
        tokenizer = AutoTokenizer.from_pretrained(
            peft_config.base_model_name_or_path,
            cache_dir=cache_dir  # 캐시 디렉토리 지정
        )
        tokenizer.pad_token = tokenizer.eos_token
        
        logger.info("모델 로딩 완료!")
        
    except Exception as e:
        logger.error(f"모델 로딩 실패: {str(e)}")
        raise e

# 애플리케이션 생명주기 관리
@asynccontextmanager
async def lifespan(app: FastAPI):
    # 시작 시 모델 로드
    logger.info("서버 시작 - 모델 로딩...")
    load_model()
    yield
    # 종료 시 정리 (필요한 경우)
    logger.info("서버 종료")

# FastAPI 앱 생성
app = FastAPI(
    title="LoRA 질의응답 API",
    description="LoRA로 파인튜닝된 한국어 질의응답 모델 API",
    version="1.0.0",
    lifespan=lifespan
)

# CORS 설정 (필요한 경우)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 프로덕션에서는 특정 도메인으로 제한
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 답변 생성 함수
def generate_answer(promptText: str, max_length: int = 256, temperature: float = 0.7, top_p: float = 0.9):
    global model, tokenizer
    
    if model is None or tokenizer is None:
        raise HTTPException(status_code=500, detail="모델이 로드되지 않았습니다.")
    
    try:
        prompt = f"""### 질문:
{promptText}
### 답변:
"""
        
        inputs = tokenizer(prompt, return_tensors="pt", padding=True, truncation=True, max_length=512)
        input_ids = inputs["input_ids"].to(model.device)
        attention_mask = inputs["attention_mask"].to(model.device)
        
        with torch.no_grad():
            output_ids = model.generate(
                input_ids=input_ids,
                attention_mask=attention_mask,
                max_length=max_length,
                do_sample=True,
                temperature=temperature,
                top_p=top_p,
                eos_token_id=tokenizer.eos_token_id,
                pad_token_id=tokenizer.pad_token_id
            )
        
        full_response = tokenizer.decode(output_ids[0], skip_special_tokens=True)
        
        # "### 답변:" 이후 부분만 추출
        if "### 답변:" in full_response:
            answer = full_response.split("### 답변:")[-1].strip()
        else:
            answer = full_response.strip()
            
        return answer
        
    except Exception as e:
        logger.error(f"답변 생성 중 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"답변 생성 실패: {str(e)}")

# API 엔드포인트들
@app.get("/", response_model=HealthResponse)
async def root():
    """서버 상태 확인"""
    return HealthResponse(
        status="running", 
        model_loaded=model is not None and tokenizer is not None
    )

@app.get("/health", response_model=HealthResponse)
async def health_check():
    """헬스 체크 엔드포인트"""
    return HealthResponse(
        status="healthy", 
        model_loaded=model is not None and tokenizer is not None
    )

@app.post("/ask", response_model=AnswerResponse)
async def ask_question(request: QuestionRequest):
    """질문에 대한 답변 생성"""
    
    if not request.promptText.strip():
        raise HTTPException(status_code=400, detail="질문이 비어있습니다.")
    
    try:
        answer = generate_answer(
            promptText=request.promptText,
            max_length=request.max_length,
            temperature=request.temperature,
            top_p=request.top_p
        )
        
        return AnswerResponse(
            answer=answer
        )
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"질문 처리 중 오류: {str(e)}")
        raise HTTPException(status_code=500, detail="서버 내부 오류가 발생했습니다.")

if __name__ == "__main__":
    # 서버 실행
    uvicorn.run(
        "main:app",  # 파일명이 main.py인 경우
        host="0.0.0.0",
        port=8082,
    )