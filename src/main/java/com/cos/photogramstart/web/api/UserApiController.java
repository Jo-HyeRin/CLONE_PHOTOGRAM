package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails){
		// jsp의 input type=file 의 name 값을 MultipartFile의 변수로 설정해줘야 데이터를 잘 받아올 수 있다.
		
		User userEntity = userService.회원프로필사진변경(principalId, profileImageFile);
		principalDetails.setUser(userEntity); // 세션 변경
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진변경 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){		
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}

	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id, 
			@Valid UserUpdateDto userUpdateDto, 
			BindingResult bindingResult, // 꼭 @Valid 다음 파라미터에 적어야 함
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		// AOP 처리했기 때문에 필요하지 않음.
		// 유효성 검사 정상 시 회원수정 진행
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errorMap = new HashMap<>();			
//			for(FieldError error : bindingResult.getFieldErrors()) {
//				errorMap.put(error.getField(), error.getDefaultMessage());
//			}			
//			throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
//		} else {
			// 회원수정
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			// 세션 업데이트
			principalDetails.setUser(userEntity); 
			return new CMRespDto<>(1, "회원수정 완료", userEntity); 
			// 응답 시 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다. images 호출로 인해 무한참조!
			// User 응담 시 Image 내부의 user getter를 호출하지 않도록 막는다 -> User.java에 @JsonIgnoreProperties 이용
//		}
		
	}
}
