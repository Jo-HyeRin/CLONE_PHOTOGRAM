package com.cos.photogramstart.handler.ex;

//Validation과 상관없는 예외처리, 여러 에러가 터질 일이 없을 것 같아서 errorMap 삭제
public class CustomApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CustomApiException(String message) {
		super(message);
	}
	
}
