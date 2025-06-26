package com.ParQ.ParQ.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ParQ.ParQ.entity.ParkingData;

public interface ParkingDataRepository extends JpaRepository<ParkingData, Long>{
	List<ParkingData> findTop24ByParkingLotOrderByTimestampDesc(String parkingLot);
	List<ParkingData> findTop168ByParkingLotOrderByTimestampAsc(String parkingLot);

}
