package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		// Repository에 modifying Query를 쓰면 성공 여부만을 리턴할 수 있다. 
		// JPA의 save 함수는 insert 후 객체를 리턴해주게 되므로 save 함수를 사용해서 구현한다.
		
		// 객체를 만들 때 필요한 값(ex:key)만 담아서 insert 할 수 있다.
		// 이렇게 하면 findById로 객체를 찾아와서 넣어주는 과정 없이 간단히 save 함수를 이용할 수 있게 된다.
		// 대신 return 시 image 객체는 id 값만 가지고 있는 가짜 객체가 리턴된다.
		Image image = new Image();
		image.setId(imageId);
		
		// username을 사용해야 하기 때문에 유저 객체는 찾아와서 넣어준다.
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
				
		// 댓글 객체 생성
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);		
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}		
	}
}
