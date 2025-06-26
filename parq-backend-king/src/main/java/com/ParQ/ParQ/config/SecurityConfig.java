package com.ParQ.ParQ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/**", "/videos/**", "/uploads/**", "/static/**", "/predict/**", "/upload")
				.permitAll().anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}
}
