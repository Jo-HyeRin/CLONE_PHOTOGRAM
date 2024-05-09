package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;

	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	// 인기 페이지
	@GetMapping("/image/popular")
	public String popular(Model model) {
		
		List<Image> images = imageService.인기사진();
		model.addAttribute("images", images);
		
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if(imageUploadDto.getFile().isEmpty()) {
			// 페이지를 리턴하기 때문에 api Exception이 아님.
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		
		// 이미지를 업로드하기 위해서 받아야 하는 정보: 캡션, 파일 -> ImageUploadDto 를 통해 정보를 받도록 하자.		
		imageService.사진업로드(imageUploadDto, principalDetails); // 서비스 호출
		return "redirect:/user/"+principalDetails.getUser().getId();
	}
}
