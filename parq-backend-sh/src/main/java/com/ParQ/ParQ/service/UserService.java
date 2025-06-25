package com.ParQ.ParQ.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ParQ.ParQ.dto.UserLoginDto;
import com.ParQ.ParQ.dto.UserRegisterDto;
import com.ParQ.ParQ.dto.UserResponseDto;
import com.ParQ.ParQ.dto.LoginResponseDto;
import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public String register(UserRegisterDto dto) {

		System.out.println("service : " + dto.getUsername());
		if (userRepository.existsByEmail(dto.getEmail())) {
			return "이미 존재하는 이메일입니다.";
		}
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		userRepository.save(user);

		return "회원가입 성공!";
	}

	public LoginResponseDto login(UserLoginDto dto) {
		Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());

		if (userOpt.isEmpty()) {
			return null; // 사용자가 없음
		}
		User user = userOpt.get();

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			return null; // 비밀번호 불일치
		}

		// 로그인 성공!
		return new LoginResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
	}
	
	public ResponseEntity<UserResponseDto> getUserInfo(long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new UserResponseDto(false, "존재하지 않는 사용자입니다.", 404));			
		}
		User user = userOpt.get();
		String message = String.format("사용자 정보: %s / %s", user.getUsername(),
				user.getEmail());
		
		return ResponseEntity.ok(new UserResponseDto(true, message, 200));
	}
}
