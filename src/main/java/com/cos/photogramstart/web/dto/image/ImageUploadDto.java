package com.cos.photogramstart.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	private MultipartFile file; // MultipartFile은 NotBlank 지원이 안 됨.
	private String caption;
	
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl) // UUID 등이 포함된 정확한 경로가 들어가야 하므로 받아온다.
				.user(user) // 업로드 유저 정보가 들어가야 하므로 받아온다.
				.build();
	}
}
