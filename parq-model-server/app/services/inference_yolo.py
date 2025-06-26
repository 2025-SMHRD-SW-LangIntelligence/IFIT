from ultralytics import YOLO
import numpy as np
import cv2
import io

model = YOLO("models/yolov8m.pt")

async def run_yolo_inference(uploaded_file):
    contents = await uploaded_file.read()
    np_img = np.frombuffer(contents, np.uint8)
    img = cv2.imdecode(np_img, cv2.IMREAD_COLOR)

    results = model(img)
    annotated = results[0].plot()

    _, buffer = cv2.imencode(".jpg", annotated)
    return io.BytesIO(buffer.tobytes())

async def run_yolo_count(uploaded_file):
    contents = await uploaded_file.read()
    np_img = np.frombuffer(contents,np.uint8)
    img = cv2.imdecode(np_img, cv2.IMREAD_COLOR)
    result = model(img)

    return sum(1 for c in result[0].boxes.cls if int(c) == 2)
