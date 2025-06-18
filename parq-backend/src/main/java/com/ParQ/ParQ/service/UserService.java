package com.ParQ.ParQ.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ParQ.ParQ.dto.UserLoginDto;
import com.ParQ.ParQ.dto.UserRegisterDto;
import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	public String register(UserRegisterDto dto) {
		
		System.out.println("service : "+dto.getUsername());
		if (userRepository.existsByEmail(dto.getEmail())) {
			return "이미 존재하는 이메일입니다.";
		}
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		userRepository.save(user);
		
		return "회원가입 성공!";
	}
	
	public String login(UserLoginDto dto) {
		Optional<User> useropt = userRepository.findByEmail(dto.getEmail());
		
		if (useropt.isEmpty()) {
			return "해당 이메일은 존재하지 않습니다.";
		}
		User user = useropt.get();
		
		if (!user.getPassword().equals(dto.getPassword())) {
			return "비밀번호가 일치하지 않습니다.";
		}
		
		return "로그인 성공!";
	}
}
