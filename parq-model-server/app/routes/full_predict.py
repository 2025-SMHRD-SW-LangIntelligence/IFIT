from fastapi import APIRouter, UploadFile, File
from app.services.inference_yolo import run_yolo_count
from app.services.inference_lstm import run_lstm_prediction, preprocess_from_db

router = APIRouter()

@router.post("/")
async def full_pipeline(file: UploadFile = File(...)):
    car_count = await run_yolo_count(file)
    updated_series = preprocess_from_db(car_count)
    result = run_lstm_prediction(updated_series)

    return {
        "car_count" : car_count,
        "predicted_congestion" : result
    }