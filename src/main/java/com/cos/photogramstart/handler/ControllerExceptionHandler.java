package com.cos.photogramstart.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;

@RestController // 데이터 응답
@ControllerAdvice // 컨트롤러에서 발생하는 모든 Exception 다 낚아챔
public class ControllerExceptionHandler {
	
	// 공통 응답 DTO 적용
//	@ExceptionHandler(CustomValidationException.class)
//	public CMRespDto<?> validationException(CustomValidationException e) {	
//		return new CMRespDto<Map<String, String>>(-1, e.getMessage(), e.getErrorMap()); 
//	}
	
	// 자바스크립트 alert, history.back 이용
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {		
		return Script.back(e.getErrorMap().toString());
	}
	
}
