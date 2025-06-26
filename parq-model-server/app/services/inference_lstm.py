import joblib
import numpy as np
import pandas as pd
from keras.models import load_model
from app.database import get_last_24_hours_data
from datetime import datetime



model = load_model("models/trained_model.h5", compile= False)
scaler_data =  joblib.load("models/scaler_data_joblib.pkl")
feature_order = scaler_data["feature_order"]
feature_scaler = scaler_data["feature_scaler"]
target_scaler = scaler_data["target_scaler"]

model.summary()


def preprocess_from_db(parkingLot:str)-> np.ndarray:

    raw_data = get_last_24_hours_data(parkingLot)

    if isinstance(raw_data, pd.DataFrame):
        if raw_data.empty:
            raise ValueError(f"❌ 주차장 '{parkingLot}'에 대한 데이터프레임이 비어 있습니다.")
    elif not raw_data:
        raise ValueError(f"❌ 주차장 '{parkingLot}'에 대한 최근 24시간 데이터가 없습니다.")

    
    df = pd.DataFrame(raw_data, columns=["timestamp", "weekday", "carIn", "carOut"])
    print("🪵 [원본 raw_data] 길이:", len(df))
    print("🪵 [raw_data 샘플]:", df.head())

    if df.shape[0] < 24:
        raise ValueError("24시간치 데이터가 부족합니다.")
    
    if df["weekday"].dtype == object:
        weekday_mapping = {
            "Monday": 0, "Tuesday": 1, "Wednesday": 2, "Thursday": 3,
            "Friday": 4, "Saturday": 5, "Sunday": 6
        }
        df["weekday"] = df["weekday"].map(weekday_mapping)

    df["timestamp"] = pd.to_datetime(df["timestamp"])
    df["hour"] = df["timestamp"].dt.hour
    df["time_sin"] = np.sin(2 * np.pi * df["hour"] / 24)
    df["time_cos"] = np.cos(2 * np.pi * df["hour"] / 24)
    df["weekday_sin"] = np.sin(2 * np.pi * df["weekday"] / 7)
    df["weekday_cos"] = np.cos(2 * np.pi * df["weekday"] / 7)

    df.rename(columns={
        "carIn": "입차대수",
        "carOut": "출차대수",
        "time_sin": "시간_sin",
        "time_cos": "시간_cos",
        "weekday_sin": "요일_sin",
        "weekday_cos": "요일_cos"
    }, inplace=True)

   
    print("📋 현재 df.columns:", df.columns.tolist())
    print("🧩 feature_order:", feature_order)

    if not set(feature_order).issubset(df.columns):
        missing = set(feature_order)-set(df.columns)
        raise ValueError(f"DataFrame 컬럼이 feature_order와 일치하지 않습니다: {df.columns.tolist()}")

    df = df[feature_order]
    print("🔍 결측값:\n", df.isnull().sum())
    if df.isnull().values.any():
        print("⚠️ 결측값이 포함되어 있습니다. 제거 후 진행합니다.")
        df = df.dropna()

    if df.shape[0] < 24:
        raise ValueError("결측값 제거 후 24시간 시퀀스가 부족합니다.")

    df_scaled = feature_scaler.transform(df)
    print("✅ 스케일된 데이터 shape:", df_scaled.shape)
    print("✅ 스케일된 값 중 NaN 포함?:", np.isnan(df_scaled).any())

    if np.isnan(df_scaled).any():
        raise ValueError("❌ 스케일링 후 24시간 시퀀스가 부족합니다.")
    
    x_scaled = np.array(df_scaled)[-24:].reshape(1, 24, len(feature_order))

    return x_scaled

def run_lstm_prediction(x: np.ndarray, steps: int = 168) -> list[int]:
    predictions = []

    for _ in range(steps):
        y_pred = model.predict(x, verbose=0)

        # 예측 결과: (1, 1, 1) → 스칼라 추출
        next_scaled = y_pred[0, -1, 0]
        next_unscaled = target_scaler.inverse_transform([[next_scaled]])[0, 0]
        predictions.append(int(round(next_unscaled)))

        # 다음 입력에 이 예측값을 사용하기 위한 피처 생성
        last_input = x[0, -1]  # (n_features,)
        next_input = last_input.copy()

        # 예: '입차대수'에 예측값을 넣는다고 가정할 때 (기타 피처는 유지)
        target_index = feature_order.index('입차대수')  # 또는 예측 대상
        next_input[target_index] = next_scaled

        # 시간 관련 피처는 1시간 후로 shift
        next_hour = (int(x.shape[1]) + len(predictions)) % 24
        next_input[feature_order.index('시간_sin')] = np.sin(2 * np.pi * next_hour / 24)
        next_input[feature_order.index('시간_cos')] = np.cos(2 * np.pi * next_hour / 24)

        # 요일 피처도 shift
        next_weekday = (datetime.now().weekday() + (len(predictions) // 24)) % 7
        next_input[feature_order.index('요일_sin')] = np.sin(2 * np.pi * next_weekday / 7)
        next_input[feature_order.index('요일_cos')] = np.cos(2 * np.pi * next_weekday / 7)

        # 입력 시퀀스를 업데이트
        x = np.append(x[:, 1:, :], [[next_input]], axis=1)

    return predictions


    # y_pred = model.predict(x)

    # if len(y_pred.shape) == 3:
    #     y_pred = y_pred.reshape(y_pred.shape[1], y_pred.shape[2])

    # print("예측: ", y_pred)    
    # y_rescaled = target_scaler.inverse_transform(y_pred).flatten()

    # print("복원된 예측", y_rescaled)
    # if np.isnan(y_rescaled).any():
    #     raise ValueError("예측결과에 nan이 포함되어 있습니다.")

    # return [int(round(val)) for val in y_rescaled]




print("DEBUG >> scaler_data keys:", scaler_data.keys())
print("DEBUG >> feature_scaler type:", type(scaler_data["feature_scaler"]))

