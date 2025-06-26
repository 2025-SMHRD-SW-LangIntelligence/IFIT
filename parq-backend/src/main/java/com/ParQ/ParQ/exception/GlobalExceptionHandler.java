package com.ParQ.ParQ.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ParQ.ParQ.dto.UserResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<UserResponseDto> handlevalidationException(
			MethodArgumentNotValidException ex){
		String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
		UserResponseDto response = new UserResponseDto(false, errorMessage, HttpStatus.BAD_REQUEST.value());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<UserResponseDto> handleException(Exception ex){
		UserResponseDto response = new UserResponseDto(false, ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
