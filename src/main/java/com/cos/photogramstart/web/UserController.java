package com.cos.photogramstart.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;

	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		// 프로필 페이지로 이동 시 모델에 유저 정보, 이미지 정보 등을 담아서 가져간다.
		UserProfileDto dto = userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		// 세션에 접근하는 방법 : @AuthenticationPrincipal 을 통해 Authentication 객체에 바로 접근할 수 있다.
//		System.out.println("세션 정보 : " + principalDetails.getUser()); // 오류 발생
		
		// 세션 정보를 직접 찾는 방법
		// 유저 정보는 세션 영역 내 SecurityContextHolder에 PrincipalDetails를 담은 Authentication이라는 객체로 들어간다
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal();
//		System.out.println("직접 찾은 세션 정보 : " + mPrincipalDetails.getUser());
		
		// 모델에 담아 세션 정보 넘겨주기 -> jsp에서는 ${principal.변수명} 으로 값을 받을 수 있음
		// 태그 라이브러리를 이용하면 세션을 모델에 담아 넘기지 않아도 됨
		// model.addAttribute("principal", principalDetails.getUser());
		return "user/update";
	}
}
