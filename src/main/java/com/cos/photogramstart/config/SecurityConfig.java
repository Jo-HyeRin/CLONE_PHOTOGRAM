package com.cos.photogramstart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 3. 해당 파일로 시큐리티를 활성화
@Configuration // 2. IOC 등록해서 메모리에 띄워야 함
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	// 1. 시큐리티 설정 파일이 되려면 WebSecurityConfigurerAdapter 를 상속해야 함
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super.configure(http); 가 기본적으로 설정되어 있다.
		// super 삭제: 기존 시큐리티 기능을 모두 비활성화 할 수 있다.
		// super.configure(http);
		
		// 설정한 주소들은 인증이 필요, 나머지는 모두 허가. 
		// 인증이 안 되면 loginPage 주소로 이동 
		// 이동한 loginPage에서 정상 로그인 시 defaultSuccessUrl로 이동
		http.authorizeRequests()
		.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**").authenticated()
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/auth/signin")
		.defaultSuccessUrl("/");		
	}
}
