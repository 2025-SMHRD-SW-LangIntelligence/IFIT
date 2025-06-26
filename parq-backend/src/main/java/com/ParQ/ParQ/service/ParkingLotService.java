package com.ParQ.ParQ.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ParQ.ParQ.dto.ParkingLotRequestDto;
import com.ParQ.ParQ.entity.ParkingLot;
import com.ParQ.ParQ.repository.ParkingLotRepository;
import com.ParQ.ParQ.repository.ParkingDataRepository;
import com.ParQ.ParQ.entity.ParkingData;

@Service
public class ParkingLotService {

	private final ParkingLotRepository parkingLotRepository;
	private final ParkingDataRepository parkingDataRepository;
	
	public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingDataRepository parkingDataRepository) {
		this.parkingLotRepository = parkingLotRepository;
		this.parkingDataRepository = parkingDataRepository;
	}
	
	public String registerParkingLot(ParkingLotRequestDto dto) {
		ParkingLot parkingLot = new ParkingLot();
		parkingLot.setName(dto.getName());
		parkingLot.setAddress(dto.getAddress());
		parkingLot.setRuntime(dto.getRuntime());
		parkingLot.setTotalSpace(dto.getTotalSpace());
		parkingLot.setFee(dto.getFee());
		
		Optional<ParkingLot> exists = parkingLotRepository.findByAddress(
				parkingLot.getAddress());
		if (exists.isPresent()) {
			return "이미 등록된 주차장입니다.";
		}
		
		parkingLotRepository.save(parkingLot);
		
		return "주차장 등록 완료!";
		
	}
	
	public List<ParkingLot> getAllParkingLots(){
		return parkingLotRepository.findAll();
	}
	
	public ParkingLot getParkingLotByName(String name) {
		return parkingLotRepository.findByName(name)
				.orElseThrow(()-> new IllegalArgumentException("해당 이름의 주차장이 존재하지 않습니다."));
	}
	
	// 특정 주차장의 가장 오래된 168개(carCount) 데이터를 시간순(오름차순)으로 반환
	public List<ParkingData> getRecentWeekCarCounts(String parkingLot) {
		return parkingDataRepository.findTop168ByParkingLotOrderByTimestampAsc(parkingLot);
	}
	
}
