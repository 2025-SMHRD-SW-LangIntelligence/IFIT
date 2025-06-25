package com.ParQ.ParQ.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ParQ.ParQ.dto.LstmRequestDto;
import com.ParQ.ParQ.dto.LstmResponseDto;

@Service
public class LstmService {

	private final RestTemplate restTemplate;
	
	public LstmService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	public LstmResponseDto predictLstm(LstmRequestDto dto) {
		String url = "http://localhost:8000/predict/lstm";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<LstmRequestDto> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<LstmResponseDto> response = restTemplate.postForEntity(url,
				requestEntity, LstmResponseDto.class);
		

		return response.getBody();
	}
}
