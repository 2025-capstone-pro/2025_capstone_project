import os
import shutil
import numpy as np
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from keras.models import load_model
from keras.metrics import MeanSquaredError
from util import extract_video_features  # 사용자 정의 함수

app = FastAPI()

# 모델 로드 (서버 시작 시 한 번만 수행)
MODEL_PATH = "autoencoder_model.h5"
try:
    autoencoder = load_model(MODEL_PATH, custom_objects={'mse': MeanSquaredError()})
    print(f"[FastAPI] 모델 '{MODEL_PATH}' 로드 성공.")
except Exception as e:
    print(f"[FastAPI] 모델 로드 실패: {e}")
    autoencoder = None

@app.post("/detect-anomaly/")
async def detect_anomaly(file: UploadFile = File(...)):
    if autoencoder is None:
        raise HTTPException(status_code=500, detail="모델이 로드되지 않았습니다.")

    # 1) 업로드된 파일을 임시 디렉토리에 저장
    temp_video_path = f"temp_{file.filename}"
    try:
        with open(temp_video_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"파일 저장 실패: {e}")

    try:
        # 2) 특징 추출
        dyn_feats = extract_video_features(temp_video_path)
        if dyn_feats is None or dyn_feats.size == 0:
            raise HTTPException(status_code=400, detail="비디오에서 특징을 추출할 수 없습니다.")

        # 3) 오토인코더로 이상치 탐지
        reconstructions = autoencoder.predict(dyn_feats)
        reconstruction_error = np.mean(np.abs(dyn_feats - reconstructions), axis=1)

        threshold = np.percentile(reconstruction_error, 95)  # 상위 5% 이상치
        anomalies = reconstruction_error > threshold

        n_reps = len(dyn_feats)
        n_outliers = int(np.sum(anomalies))
        outlier_ratio = float(n_outliers / n_reps)

        # 4) 결과 반환
        result = {
            "total_reps": n_reps,
            "anomalies_detected": n_outliers,
            "anomaly_ratio": round(outlier_ratio, 4),
            "anomaly_detected": n_outliers > 0
        }
        return JSONResponse(content=result)

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"처리 중 오류 발생: {e}")

    finally:
        # 5) 임시 파일 삭제
        if os.path.exists(temp_video_path):
            os.remove(temp_video_path)
