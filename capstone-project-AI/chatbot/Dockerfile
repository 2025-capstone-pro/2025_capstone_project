FROM python:3.9-slim

WORKDIR /app

# 시스템 패키지 업데이트 (캐시 활용)
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# requirements.txt 복사 및 설치
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 코드 복사
COPY . .

# HuggingFace 캐시를 위한 볼륨 생성
VOLUME ["/root/.cache/huggingface"]

# 포트 노출
EXPOSE 8082

# 애플리케이션 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8082"]