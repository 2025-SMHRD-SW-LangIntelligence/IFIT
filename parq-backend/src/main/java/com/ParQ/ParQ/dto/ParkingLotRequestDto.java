package com.ParQ.ParQ.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotRequestDto {
	private String name;
	private String address;
	private String runtime;
	private Integer total_space;
	private Integer fee;
}
