package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	
	// 데이터베이스에 대한 어노테이션
	// 스키마 변경 시 그냥 저장하면 반영되지 않는다.
	// 다시 create로 실행해야 반영됨.
	@Column(length=20, unique=true)
	private String username;	
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String name;
	private String website; // 웹사이트
	private String bio; // 자기소개
	@Column(nullable=false)
	private String email;
	private String phone;
	private String gender; // 성별
	private String profileImageUrl; // 사진
	private String role; // 권한		
	
	// OneToMany: 한 명의 유저는 여러 이미지를 만들 수 있음, mappedBy: 나는 연관관계의 주인이 아니다. 주인은 Image의 user야. 그러니 테이블에 컬럼 만들지 마.
	// User를 Select할 때 해당 User id로 등록된 image들을 다 가져와
	// LAZY: User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 대신 getImages()함수의 image들이 호출될 때 가져와.
	// EAGER = User를 Select할 때 해당 User id로 등록된 image들을 전부 Join 해서 가져와.
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY) 
	@JsonIgnoreProperties({"user"}) // Image 내부의 user getter는 호출하지 마.
	private List<Image> images; // 양방향 매핑
	
	private LocalDateTime createDate;
	
	@PrePersist // DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	// system.out.print 사용 시 user를 뿌리면 무한참조로 인해 오류가 발생한다. 이는 @JsonIgnoreProperties로 해결할 수 없다.
	// 따라서 toString을 Override해서 images를 삭제하여 해결한다.
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate=" + createDate + "]";
	}
	
	
}
