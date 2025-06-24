import numpy as np
from keras.models import load_model
import joblib
import json
from sklearn.preprocessing import LabelEncoder

model = load_model("models/trained_model.h5", compile= False)
le : LabelEncoder = joblib.load("models/label_encoder.pkl")
scaler = joblib.load("models/scaler_dict.pkl")
with open("models/feature_order.json", encoding="utf-8") as f:
    feature_order = json.load(f)


def preprocess(parking_lot: str, counts: list[int])-> np.ndarray:
    assert len(counts) == 24
    encoded_name = le.transform([parking_lot])[0]
    features = {"주차장명": encoded_name}
    
    for i in range(24):
        features[f"{i:02d}시"] = counts[i]

    x_input_list = [features[k] for k in feature_order]
    x_scaled = [
        (x - scaler[k]["mean"])/scaler[k]["std"]
        for x, k in zip(x_input_list, feature_order)
    ]
    
    return np.array([x_scaled])

def run_lstm_prediction(data) -> dict:
    predictions = model.predict(data)[0]
    result = {}
    for i, val in enumerate(predictions):
        hour_label =f"{i:02d}시"
        result[hour_label] = int(round(val))

    return result




