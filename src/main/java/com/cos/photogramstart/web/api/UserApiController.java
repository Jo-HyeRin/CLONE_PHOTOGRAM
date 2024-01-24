package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;

	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id, 
			@Valid UserUpdateDto userUpdateDto, 
			BindingResult bindingResult, // 꼭 @Valid 다음 파라미터에 적어야 함
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		// 유효성 검사 정상 시 회원수정 진행
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}			
			throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
		} else {
			// 회원수정
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			// 세션 업데이트
			principalDetails.setUser(userEntity); 
			return new CMRespDto<>(1, "회원수정 완료", userEntity); 
			// 응답 시 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다. images 호출로 인해 무한참조!
			// User 응담 시 Image 내부의 user getter를 호출하지 않도록 막는다 -> User.java에 @JsonIgnoreProperties 이용
		}
		
	}
}
