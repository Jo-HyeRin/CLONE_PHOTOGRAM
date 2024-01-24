package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 프로필 페이지 회원 정보 가져오기
	// 서비스가 종료되는 시점에 영속성 컨텍스트는 변경된 오브젝트를 감지하고 DB로 Flush 한다.(=더티체킹)
	// 조회 시 트랜잭션 태울 때 readOnly = true 걸어주면 변경 감지를 하지 않게 되므로 일을 덜 하게 된다.
	@Transactional(readOnly = true) 
	public User 회원프로필(int userId) {
		// SELECT * FROM image WHERE userId =:userId;
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		return userEntity;
	}

	// 회원수정
	@Transactional
	public User 회원수정(int id, User user) {
		// 영속화 : 영속성 컨텍스트에 정보를 찾아서 입력
		// get() : 무조건 찾았다 걱정마
		// User userEntity = userRepository.findById(id).get(); 		
		// orElseThrow() : 못찾으면 익셉션 발동시킬게
//		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//			@Override
//			public IllegalArgumentException get() {
//				return new IllegalArgumentException("찾을 수 없는 id 입니다");
//			}			
//		});
		// orElseThrow() 람다식으로 작성, 예외는 공통으로 처리
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
			return new CustomValidationApiException("찾을 수 없는 id 입니다");
		});
		
		// 수정 정보 입력
		userEntity.setName(user.getName());
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());		
		
		return userEntity;
	} // 더티체킹 일어나서 업데이트 완료
	
}
