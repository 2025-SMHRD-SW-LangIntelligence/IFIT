package com.ParQ.ParQ.entity;

import jakarta.persistence.Column;
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
@Table(name = "parkingLots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="parkingLotId")
	private Long id;
	
	private String name;
	private String address;
	private String runtime;
	
	@Column(name = "totalSpace")
	private int totalSpace;
	private int fee;

}
