import numpy as np
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import JSONResponse
from keras.models import load_model
from keras.metrics import MeanSquaredError
from typing import Dict, Any
import json
from util_infer import parse_landmarks_json, extract_features_from_landmarks  # (N, 33, 3) 파싱 함수

app = FastAPI()

# 모델 로드
MODEL_PATH = "autoencoder_model.h5"
try:
    autoencoder = load_model(MODEL_PATH, custom_objects={'mse': MeanSquaredError()})
    print(f"[FastAPI] 모델 '{MODEL_PATH}' 로드 성공.")
except Exception as e:
    print(f"[FastAPI] 모델 로드 실패: {e}")
    autoencoder = None

@app.post("/detect-anomaly/")
async def detect_anomaly(request: Request):
    if autoencoder is None:
        raise HTTPException(status_code=500, detail="모델이 로드되지 않았습니다.")

    try:
        # 1) JSON 요청 데이터 파싱
        json_data: Dict[str, Any] = await request.json()

        # 2) parse_landmarks_json 함수 호출 (사전형 데이터 바로 넘기기)
        landmark_array: np.ndarray = parse_landmarks_json(json_data)
        dyn_feats = extract_features_from_landmarks(landmark_array)
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
        raise HTTPException(status_code=400, detail=f"요청 처리 실패: {str(e)}")
