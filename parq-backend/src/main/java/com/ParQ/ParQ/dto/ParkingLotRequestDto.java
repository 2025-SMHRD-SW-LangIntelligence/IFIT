package com.ParQ.ParQ.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotRequestDto {
	private String name;
	
	@NotNull(message = "주소는 필수입니다.")
	private String address;
	private String runtime;

	@NotNull(message = "총 주차공간 수는 필수입니다.")
	private Integer totalSpace;
	private Integer fee;
}
