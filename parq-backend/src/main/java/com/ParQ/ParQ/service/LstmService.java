package com.ParQ.ParQ.service;

import java.util.List;
import java.util.Map;

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

	public LstmResponseDto predictLstm(LstmRequestDto dto) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8000/predict/lstm/";

        String parkingLotName = dto.getParkingLot();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LstmRequestDto> requestEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity,
        		Map.class);
        
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("prediction")) {
            throw new IllegalStateException("FastAPI 응답에 'prediction' 키가 없습니다.");
        }

        @SuppressWarnings("unchecked")
        List<Integer> prediction = (List<Integer>) responseBody.get("prediction");

        
        
        return new LstmResponseDto(parkingLotName, prediction);
    }
}
