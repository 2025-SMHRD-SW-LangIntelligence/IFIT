import pandas as pd
from sqlalchemy import create_engine

# CSV 파일 불러오기
df = pd.read_csv("/Users/jeong-wangsu/git/IFIT/parq-model-server/DATA/학습2.csv")

# 컬럼명 CamelCase로 변환
df.columns = ["parkingLot", "timestamp", "weekday", "carIn", "carOut", "carCount"]

# DB 연결 정보 설정
db_user = "campus_25SW_LI_p2_4"
db_password = "smhrd4"  # 👉 실제 비밀번호로 바꾸세요
db_host = "project-db-campus.smhrd.com"
db_port = "3307"
db_name = "campus_25SW_LI_p2_4"      # 👉 실제 DB 이름으로 바꾸세요

# SQLAlchemy 엔진 생성
engine = create_engine(f"mysql+pymysql://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}")

# 데이터 삽입 (parkingData 테이블)
df.to_sql("parkingData", con=engine, index=False, if_exists="append")
