from fastapi import FastAPI
from app.routes.predict_yolo import router as yolo_router
from app.routes.predict_lstm import router as lstm_router
from app.routes.full_predict import router as full_router


app = FastAPI()

app.include_router(yolo_router, prefix="/predict/yolo")
app.include_router(lstm_router, prefix="/predict/lstm")
app.include_router(full_router, prefix="/predict/full")
