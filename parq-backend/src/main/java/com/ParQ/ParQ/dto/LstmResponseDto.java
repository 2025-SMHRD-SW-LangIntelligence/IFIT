package com.ParQ.ParQ.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LstmResponseDto {
	private String parkingLot;
	private List<Integer> prediction;
	
}
