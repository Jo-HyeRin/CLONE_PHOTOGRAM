package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;

	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		// 네이티브 쿼리 실행
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		// 삭제는 오류가 발생할 일이 없을 것 같아 예외처리 하지 않음. 추후 try-catch로 예외처리 하면 됨.
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
