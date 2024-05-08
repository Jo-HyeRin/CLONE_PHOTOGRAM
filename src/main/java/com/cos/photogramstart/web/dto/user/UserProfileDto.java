package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	private boolean pageOwnerState; // 페이지 주인 여부
	private int imageCount; // 게시물(이미지) 수
	private boolean subscribeState; // 구독 상태
	private int subscribeCount; // 구독 수
	private User user;
}
