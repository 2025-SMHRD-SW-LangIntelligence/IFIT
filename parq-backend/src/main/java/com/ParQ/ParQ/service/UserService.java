package com.ParQ.ParQ.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	public User registerUser(User user) {
		
		System.out.println("service : "+user.getUsername());
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		return userRepository.save(user);
	}
}
