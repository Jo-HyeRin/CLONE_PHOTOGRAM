package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 3. 해당 파일로 시큐리티를 활성화
@Configuration // 2. IOC 등록해서 메모리에 띄워야 함
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	// 1. 시큐리티 설정 파일이 되려면 WebSecurityConfigurerAdapter 를 상속해야 함
	
	// 비밀번호 암호화
	@Bean // SecurityConfig가 IoC에 등록될 때 @Bean을 읽어서 return 해서 IoC가 들고 있다.
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super.configure(http); 가 기본적으로 설정되어 있다.
		// super 삭제: 기존 시큐리티 기능을 모두 비활성화 할 수 있다.
		// super.configure(http);
		
		// 클라이언트가 페이지를 요청했을 때, 시큐리티는 페이지에 CSRF 토큰을 심어서 응답한다.
		// 클라이언트가 POST 재요청 할 때, 시큐리티는 CSRF 토큰의 일치 여부를 확인한다.(포스트맨 요청은 비정상이겠다!)
		// ex) 로그인-회원가입(폼)-가입(버튼)
		// 수월한 작업을 위해서 우선 CSRF 토큰 비활성화
		http.csrf().disable();
		
		// 설정한 주소들은 인증이 필요, 나머지는 모두 허가. 
		// 인증이 안 되면 loginPage 주소로 이동 
		// 이동한 loginPage에서 정상 로그인 시 defaultSuccessUrl로 이동
		http.authorizeRequests()
		.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**").authenticated()
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/auth/signin") // GET 요청 시
		.loginProcessingUrl("/auth/signin") // POST 요청 시 -> 스프링 시큐리티가 로그인 프로세스 진행
		.defaultSuccessUrl("/");		
	}
}
