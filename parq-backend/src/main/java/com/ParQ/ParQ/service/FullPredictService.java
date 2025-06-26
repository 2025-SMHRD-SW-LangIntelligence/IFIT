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
public class FullPredictService {
	
	private final RestTemplate restTemplate;
	
	public FullPredictService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	public String predictFull(MultipartFile file) throws IOException {
		String url = "http://localhost:8000/predict/full";
		return sendMultipart(file, url, String.class);
	}
	
	public <T> T sendMultipart(MultipartFile file, String url, Class<T> responseType)
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
