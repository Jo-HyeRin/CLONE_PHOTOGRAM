package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CommentDto {
	// 유효성 검사 적용 시
	// @NotBlank: 빈 값, null, 빈 공백(스페이스바) 체크
	// @NotEmpty: 빈 값, null 체크
	// @NotNull: null 체크
	
	@NotBlank
	private String content;
	@NotNull
	private Integer imageId; // int/integer는 null 체크만 할 수 있다.
	
	// toEntity가 필요하지 않음.
}
