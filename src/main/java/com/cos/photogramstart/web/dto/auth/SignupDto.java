package com.cos.photogramstart.web.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data // Getter, Setter를 만들어주는 어노테이션
public class SignupDto {
	// @valid에 대한 어노테이션 적용	시 validation 체크 가능
	@Size(min=2, max=20)
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	
	// 받은 데이터를 유저 객체로 리턴
	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}
