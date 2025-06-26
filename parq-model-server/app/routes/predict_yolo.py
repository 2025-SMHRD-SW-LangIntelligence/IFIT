from fastapi import APIRouter, UploadFile, File, Response, HTTPException
from app.services.inference_yolo import run_yolo_inference

router = APIRouter()

@router.post("/")
async def detect_objects(file: UploadFile = File(...)):
    try:
        annotated_image = await run_yolo_inference(file)
        return Response(content=annotated_image.getvalue(), media_type="image/jpeg")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))