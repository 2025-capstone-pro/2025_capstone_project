FROM python:3.9-slim

WORKDIR /app

# 시스템 패키지 설치 (한 번에 처리하고 캐시 정리)
RUN apt-get update && apt-get install -y \
    build-essential \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# requirements.txt 복사 및 설치
COPY requirements.txt .

# pip 캐시 활용하여 패키지 설치
RUN --mount=type=cache,target=/root/.cache/pip pip install -r requirements.txt

# 애플리케이션 코드 복사
COPY . .

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8081"]
