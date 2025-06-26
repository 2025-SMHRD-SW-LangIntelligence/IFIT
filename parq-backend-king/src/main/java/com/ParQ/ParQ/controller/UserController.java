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
import java.util.Optional;

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
		Optional<com.ParQ.ParQ.entity.User> userOpt = userService.getUserRepository().findByEmail(dto.getEmail());
		if (userOpt.isEmpty()) {
			return ResponseEntity.ok("해당 이메일은 존재하지 않습니다.");
		}
		com.ParQ.ParQ.entity.User user = userOpt.get();
		if (!userService.getPasswordEncoder().matches(dto.getPassword(), user.getPassword())) {
			return ResponseEntity.ok("비밀번호가 일치하지 않습니다.");
		}
		// 로그인 성공 시 JSON 반환
		String json = String.format("{\"id\":%d,\"username\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}",
				user.getId(), user.getUsername(), user.getEmail(), user.getRole());
		return ResponseEntity.ok(json);
	}
	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getMyInfo(@RequestParam Long userId) {
		return userService.getUserInfo(userId);
	}
}
