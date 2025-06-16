package com.ParQ.ParQ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController

public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public String register(@RequestParam String username, 
			@RequestParam String email, @RequestParam String password) {
		User user = userService.registerUser(null);
		
		
		return "";
	}
		
}
