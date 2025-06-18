package com.ParQ.ParQ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ParQ.ParQ.dto.UserLoginDto;
import com.ParQ.ParQ.dto.UserRegisterDto;
import com.ParQ.ParQ.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRegisterDto dto){
		String result = userService.register(dto);
		return ResponseEntity.ok(result);
	}
		
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginDto dto) {
		String result = userService.login(dto);
		return ResponseEntity.ok(result);
	}
}
