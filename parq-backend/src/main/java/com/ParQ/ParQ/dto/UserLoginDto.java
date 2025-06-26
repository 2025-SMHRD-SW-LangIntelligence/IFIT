package com.ParQ.ParQ.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@NotNull(message = "이메일 입력은 필수입니다.")
	private String email;
	
	@NotNull(message ="비밀번호를 입력하세요.")
	private String password;
}
