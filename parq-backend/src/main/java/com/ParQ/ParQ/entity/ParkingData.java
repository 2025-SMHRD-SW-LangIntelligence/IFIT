package com.ParQ.ParQ.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parkingData")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String parkingLot;
	private LocalDateTime timestamp;
	private String weekday;
	private Integer carIn;
	private Integer carOut;
	private Integer carCount;
	
	
}
