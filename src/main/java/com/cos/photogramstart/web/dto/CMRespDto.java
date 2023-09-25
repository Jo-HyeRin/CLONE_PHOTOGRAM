package com.cos.photogramstart.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CMRespDto<T> { // 전역에서 사용하는 공통 응답 DTO : 제네릭 사용(다양한 타입을 리턴하기 위해서)
	private int code; // 1(성공), -1(실패)
	private String message;
	private T data;
}
