package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // 특별한 역할 없어도 메모리에 띄울 수 있는 어노테이션. RestController, Service 등은 Component를 상속해서 만들어져 있음.
@Aspect
public class ValidationAdvice { // 공통 기능
	// @Before: 함수 실행 전에 관여
	// @After: 함수 실행 후에 관여
	// @Around: 함수 실행 전,후 모두 관여
	
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") // api 컨트롤러 내 모든 메서드에 적용
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// 메서드의 매개변수에 접근
		Object[] args = proceedingJoinPoint.getArgs(); 
		// 유효성 검사
		for(Object arg : args) {
			if(arg instanceof BindingResult) { // BindingResult 타입의 매개변수이면(유효성 검사를 하는 매개변수이면)	
				// 다운캐스팅
				BindingResult bindingResult = (BindingResult) arg; 
				// 유효성 검사 실패 시
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();			
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}			
					throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
				}				
			}
		}
		
		// ProceedingJoinPoint: 메서드 모든 곳(파라미터 포함)에 접근할 수 있는 변수
		// 메서드 보다 먼저 실행
		return proceedingJoinPoint.proceed(); // 메서드 실행 시작(해당 메서드로 돌아감)
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))") // 컨트롤러 내 모든 메서드에 적용
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg; 
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();			
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}			
					throw new CustomValidationException("유효성 검사 실패함", errorMap);
				}	
			}
		}		
		return proceedingJoinPoint.proceed();
	}
}
