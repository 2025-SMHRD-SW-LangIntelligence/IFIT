 package com.ParQ.ParQ.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ParQ.ParQ.dto.UserLoginDto;
import com.ParQ.ParQ.dto.UserRegisterDto;
import com.ParQ.ParQ.dto.UserResponseDto;
import com.ParQ.ParQ.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDto dto) {
		String result = userService.register(dto);
		return ResponseEntity.ok(new UserResponseDto(true, result,200));
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginDto dto) {
		String result = userService.login(dto);
		return ResponseEntity.ok(result);
	}
	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getMyInfo(@RequestParam Long userId) {
		return userService.getUserInfo(userId);
	}
}
