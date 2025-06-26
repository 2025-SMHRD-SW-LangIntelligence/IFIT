import os
import pandas as pd
import numpy as np
from dotenv import load_dotenv
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
from datetime import datetime, timedelta

load_dotenv()

DATABASE_URL = os.getenv("DATABASE_URL")
if DATABASE_URL is None:
    raise ValueError("DATABASE_URL is not set. Check your .env file.")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(bind=engine)

def get_last_24_hours_data(parkingLot: str) -> pd.DataFrame:
    session = SessionLocal()

    try:
        max_time_sql = text("""
            SELECT MAX(timestamp) AS maxTimestamp
            FROM parkingData
            WHERE parkingLot = :parkingLot
        """)
        max_time_result = session.execute(max_time_sql, {"parkingLot": parkingLot})
        max_timestamp = max_time_result.scalar()

        if not max_timestamp:
            raise ValueError(f"No data found for parkingLot: {parkingLot}")

        start_time = max_timestamp - timedelta(hours=24)

        # 필요한 컬럼만 조회
        data_sql = text("""
            SELECT timestamp, weekday, carIn, carOut
            FROM parkingData
            WHERE parkingLot = :parkingLot
              AND timestamp BETWEEN :start AND :end
            ORDER BY timestamp ASC
        """)
        result = session.execute(data_sql, {
            "parkingLot": parkingLot,
            "start": start_time,
            "end": max_timestamp
        })

        # DataFrame으로 변환
        rows = result.fetchall()
        df = pd.DataFrame(rows, columns=["timestamp", "weekday", "carIn", "carOut"])
        return df
    
    finally:
       session.close()