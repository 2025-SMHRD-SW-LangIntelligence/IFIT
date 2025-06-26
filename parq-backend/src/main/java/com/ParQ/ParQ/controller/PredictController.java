package com.ParQ.ParQ.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ParQ.ParQ.dto.LstmRequestDto;
import com.ParQ.ParQ.dto.LstmResponseDto;
import com.ParQ.ParQ.service.LstmService;
import com.ParQ.ParQ.service.ModelService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictController {
	
	private final ModelService modelService;
	private final LstmService lstmService;
	
	@PostMapping("/yolo")
	public void predictYolo(@RequestPart MultipartFile file, HttpServletResponse response)
	throws IOException {
		byte[] result = modelService.predictYolo(file);
		response.setContentType("image/jpeg");
		response.getOutputStream().write(result);
	}
	
	@PostMapping("/lstm")
	public ResponseEntity<LstmResponseDto> predictLstm(@RequestBody LstmRequestDto dto) {
		LstmResponseDto response = lstmService.predictLstm(dto);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/full")
	public String predictFull(@RequestPart MultipartFile file) throws IOException {
		return modelService.predictFull(file);
	}
}
