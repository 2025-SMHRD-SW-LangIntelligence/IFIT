package com.ParQ.ParQ.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LstmRequestDto {
	private String 주차장명;
	private String 예측날짜;
	private List<Integer> 전날_차량대수;
	
}