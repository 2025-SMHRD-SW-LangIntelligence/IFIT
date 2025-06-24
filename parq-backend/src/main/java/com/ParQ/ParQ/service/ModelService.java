package com.ParQ.ParQ.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ParQ.ParQ.dto.LstmRequestDto;
import com.ParQ.ParQ.dto.LstmResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelService {
	
	private final YoloService yoloService;
	private final LstmService lstmService;
	private final FullPredictService fullPredictService;
	
	public byte[] predictYolo(MultipartFile file) throws IOException{
		return yoloService.predictYolo(file);
	}
	
	public LstmResponseDto predictLstm(LstmRequestDto dto) {
		return lstmService.predictLstm(dto);
	}
	
	public String predictFull(MultipartFile file) throws IOException {
		return fullPredictService.predictFull(file);
	}
		
}
