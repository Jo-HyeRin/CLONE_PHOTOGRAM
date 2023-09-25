package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
	// 로그인 POST 요청이 들어오면 시큐리티 설정파일이 낚아채서 Body Data(username,password)를 이용해
	// IOC Container에 있는 UserDetailsService로 로그인 진행한다.
	// PrincipalDetailsService를 implements UserDetailsService 해서 만들고 @Service 붙이면
	// IOC Container에 있는 UserDetailsService와 타입이 같으므로 덮어써버림
	// 즉, 내가 만든 PrincipalDetailsService에서 로그인이 진행되게 된다.
	
	private final UserRepository userRepository;
	
	// 패스워드는 시큐리티가 알아서 비교해주기 때문에, username이 있는지 없는지만 확인하면 된다.
	// 리턴이 잘되면 자동으로 UserDetails 타입 세션을 만들어준다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			return null;
		} else {
			// UserDetails 타입을 리턴하기엔 매우 복잡, PrincipalDetails를 만들자
			return new PrincipalDetails(userEntity);
		}
		
	}

}
