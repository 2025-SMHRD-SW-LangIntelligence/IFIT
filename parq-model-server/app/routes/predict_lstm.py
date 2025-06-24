from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.inference_lstm import (run_lstm_prediction, preprocess)
import numpy as np

router = APIRouter()

class RequestData(BaseModel):
    주차장명: str
    예측날짜: str
    전날_차량대수: list[int]


@router.post("/")
async def predict_congestion(data: RequestData):
  
    x_input = preprocess(data.주차장명, data.전날_차량대수)
    result = run_lstm_prediction(x_input)

    return {
        "주차장명": data.주차장명,
        "예측날짜": data.예측날짜,
        "예측결과": result
            }