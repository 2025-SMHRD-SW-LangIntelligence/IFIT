package com.ParQ.ParQ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.service.UserService;


@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public String register(@RequestParam String username, 
			@RequestParam String email, @RequestParam String password) {
		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
				
				
		System.out.println("controller : "+user.getPassword());
		User registed=	userService.registerUser(user);
		
		
		
		return "redirect:/";
	}
		
}
