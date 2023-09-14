package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 1. IoC 등록 2. 트랜잭션 관리
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화
	
	@Transactional
	public User 회원가입(User user) {
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER"); // 관리자 ROLE_ADMIN		
		// 회원가입 진행
		User userEntity = userRepository.save(user);
		// user: 외부에서 받은 데이터 / userEntity: DB에서 받은 데이터
		return userEntity;
	}
}
