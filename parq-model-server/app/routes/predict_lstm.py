from fastapi import APIRouter, HTTPException, Query
from app.services.inference_lstm import run_lstm_prediction, preprocess_from_db
from pydantic import BaseModel


router = APIRouter()

class LstmRequestDto(BaseModel):
    parkingLot: str

@router.post("/")
async def predict_lstm(dto: LstmRequestDto):
    try:
        
        x = preprocess_from_db(dto.parkingLot)
        result = run_lstm_prediction(x)

        return {
                "parkingLot": dto.parkingLot,
                "prediction": result
            }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))