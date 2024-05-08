package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int id; // 아이디
	private String username; // 이름
	private String profileImageUrl; // 프로필 이미지 경로
	// MariaDB는 true 값을 int로 받지 못하므로 Integer로 받아준다.
	private Integer subscribeState; // 구독 상태
	private Integer equalUserState; // 로그인 유저와 같은 지 아닌 지
}
