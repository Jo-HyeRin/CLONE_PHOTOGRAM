package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API(자바로 데이터를 영구적 저장(DB에) 할 수 있는 API를 제공)

@Builder
@AllArgsConstructor // 전체생성자
@NoArgsConstructor // 빈생성자
@Data
@Entity // 디비에 테이블을 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 자동 증가 전략이 데이터베이스를 따라간다.
	private int id;	
	
	// 스키마 변경 시 그냥 저장하면 반영되지 않는다.
	// 다시 create로 실행해야 반영됨.
	@Column(length=20, unique=true)
	private String username;	
	private String password;
	private String name;
	private String website; // 웹사이트
	private String bio; // 자기소개
	private String email;
	private String phone;
	private String gender; // 성별
	private String profileImageUrl; // 사진
	private String role; // 권한	
	private LocalDateTime createDate;
	
	@PrePersist // DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
