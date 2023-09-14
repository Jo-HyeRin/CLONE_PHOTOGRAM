package com.cos.photogramstart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI할 때 사용(final이 걸려있는 모든 것들의 생성자를 만들어 줌)
@Controller // 1. IoC 등록 2. 파일을 리턴하는 컨트롤러
public class AuthController {	
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	// DI할 때 
	// 1. @Autowired 어노테이션 이용 
	// @Autowired
	// private AuthService authService;
	
	// 2. AuthController의 생성자 생성(AuthService를 받는)
	// @Controller가 붙어있으면 컨트롤러를 관리하는 메모리에 메모리 객체를 생성해서 로드함
	// 객체를 생성하는 첫 번째 조건이 생성자를 만들고 실행해야 한다.	
	// 이 생성자에 IoC 컨테이너에 있는 AuthService를 가져와서 주입해야한다.
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}
	
	// 3. 2번이 귀찮으니, final, @RequiredArgsConstructor를 붙여 사용한다.
	private final AuthService authService; // IoC에 등록되어 있는 것을 가져와서 주입해준다.		

	// 로그인 페이지
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	// 회원가입 페이지
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 회원가입
	@PostMapping("/auth/signup")
	public String signup(SignupDto signupDto) { 
		// 스프링은 기본적으로 key-value(x-www-form-urlencoded)형식으로 데이터를 받음
		// log.info(signupDto.toString());
		
		// User 타입으로 데이터 받음
		User user = signupDto.toEntity();
		// log.info(user.toString());
		
		// User 테이블에 signupDto 값을 저장
		User userEntity = authService.회원가입(user);
		//System.out.println(userEntity);
		
		// 회원가입 후 로그인 페이지로 이동
		return "auth/signin";
	}
	
}
