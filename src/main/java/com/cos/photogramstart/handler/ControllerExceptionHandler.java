package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController // 데이터 응답
@ControllerAdvice // 컨트롤러에서 발생하는 모든 Exception 다 낚아챔
public class ControllerExceptionHandler {
	
	// 공통 응답 DTO 적용
//	@ExceptionHandler(CustomValidationException.class)
//	public CMRespDto<?> validationException(CustomValidationException e) {	
//		return new CMRespDto<Map<String, String>>(-1, e.getMessage(), e.getErrorMap()); 
//	}
	
	// 자바스크립트 응답 - alert, history.back 이용
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {			
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	// 데이터 응답
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	// validation 외 데이터 응답 예외처리
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
}
