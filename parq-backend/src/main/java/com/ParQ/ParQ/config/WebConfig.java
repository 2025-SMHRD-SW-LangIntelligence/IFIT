package com.ParQ.ParQ.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/videos/**")
			.addResourceLocations("classpath:/static/videos/");
		
		registry
			.addResourceHandler("/uploads/**")
			.addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
		
	}
}
