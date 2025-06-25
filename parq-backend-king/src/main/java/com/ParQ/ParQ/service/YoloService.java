package com.ParQ.ParQ.service;

import java.io.IOException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class YoloService {
	
	private final RestTemplate restTemplate;
	
	public YoloService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build(); // TODO: (optional) REST call 에 대한 time out 세팅을 두는 것이 일반적이에요
	}
	
	public byte[] predictYolo(MultipartFile file) throws IOException {
		String url = "http://localhost:8000/predict/yolo";
		return sendMultipart(file, url, byte[].class);
	}
	
	private <T> T sendMultipart(MultipartFile file, String url, Class<T> responseType)
	throws IOException{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		};
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", resource);
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<T> response = restTemplate.postForEntity(url, entity, responseType);
		return response.getBody();
		
	}
}
