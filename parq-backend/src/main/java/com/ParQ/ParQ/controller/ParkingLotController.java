package com.ParQ.ParQ.controller;

import java.util.List;
import java.net.URLDecoder;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ParQ.ParQ.dto.ParkingLotRequestDto;
import com.ParQ.ParQ.entity.ParkingLot;
import com.ParQ.ParQ.entity.ParkingData;
import com.ParQ.ParQ.service.ParkingLotService;

@RestController
@RequestMapping("/api/parkinglots")
public class ParkingLotController {
	
	private final ParkingLotService parkingLotService;
	
	public ParkingLotController(ParkingLotService parkingLotService) {
		this.parkingLotService = parkingLotService;
		
	}
	
	@PostMapping
	public ResponseEntity<String> registerParkingLot(
			@RequestBody ParkingLotRequestDto dto) {
		String result = parkingLotService.registerParkingLot(dto);
		return ResponseEntity.ok(result);
		
	}
	
	@GetMapping
	public ResponseEntity<List<ParkingLot>> getAllParkingLots(){
		return ResponseEntity.ok(parkingLotService.getAllParkingLots());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<ParkingLot> getParkingLotByName(
			@PathVariable String name) {
		ParkingLot lot = parkingLotService.getParkingLotByName(name);
		return ResponseEntity.ok(lot);
	}
	
	// 특정 주차장의 최근 7일치(168개) 시간대별 carCount 데이터 반환
	@GetMapping("/{name}/congestion/week")
	public ResponseEntity<List<ParkingData>> getRecentWeekCarCounts(@PathVariable String name) {
		System.out.println("[API 호출됨] /api/parkinglots/" + name + "/congestion/week");
		try {
			String decodedName = URLDecoder.decode(name, "UTF-8");
			System.out.println("디코딩된 name: " + decodedName);
			List<ParkingData> data = parkingLotService.getRecentWeekCarCounts(decodedName);
			System.out.println("조회된 데이터 개수: " + data.size());
			return ResponseEntity.ok(data);
		} catch (Exception e) {
			System.out.println("에러 발생: " + e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}
}
