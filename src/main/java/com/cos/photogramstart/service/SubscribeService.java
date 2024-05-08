package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체. 여기서 구현체를 바로 만들기 위해 DI해줌.
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId){		
		// SubscribeRepository는 Subscribe 타입을 받기 때문에 SubscribeDto를 적용하는 구독리스트는 여기서 직접 네이티브 쿼리를 작성한다.
		// 1. 파라미터 자리에 ?를 넣고 쿼리 작성
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, "); // 끝에 꼭 한 칸을 띄워줘야 오류 발생 확률을 줄일 수 있다!
		sb.append("IF((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) AS subscribeState, ");
		sb.append("IF((? = u.id), 1, 0) AS equalUserState ");
		sb.append("FROM user U ");
		sb.append("INNER JOIN subscribe s ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); // 세미콜론을 첨부하면 안 된다!
		
		// 2. javax.persistence.Query 를 사용하여 파라미터 바인딩하고 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		// 3. 쿼리 실행(한 건인 경우 uniqueResult, 다 건인 경우 list)
		// org.qlrm.mapper.JpaResultMapper -> qlrm 이라는 라이브러리를 사용함(pom.xml에 추가되어있음)
		// qlrm: 데이터베이스 결과값을 자바클래스에 맵핑해주는 라이브러리
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		return subscribeDtos;
	}

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
