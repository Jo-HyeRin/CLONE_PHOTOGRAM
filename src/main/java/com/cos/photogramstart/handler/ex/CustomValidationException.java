package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

	// 객체를 구분할 때 사용하는 시리얼넘버 (JVM에게 중요)
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Map<String, String> errorMap;
	
	public CustomValidationException(String message, Map<String, String> errorMap) {
		super(message); // message는 부모에게 던지기(getter 필요없음)	
		this.errorMap = errorMap;
	}
	
	public Map<String, String> getErrorMap(){
		return errorMap;
	}
	
}
