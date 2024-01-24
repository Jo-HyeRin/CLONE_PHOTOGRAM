package com.cos.photogramstart.handler.ex;

public class CustomException extends RuntimeException {

	// 객체를 구분할 때 사용하는 시리얼넘버 (JVM에게 중요)
	private static final long serialVersionUID = 1L;
	
	public CustomException(String message) {
		super(message); // message는 부모에게 던지기(getter 필요없음)
	}	
}
