package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String caption; // 내용
	private String postImageUrl; // 사진을 전송받아서 그 사진을 서버 폴더에 저장 - DB에는 그 저장 경로를 insert
	
	@JsonIgnoreProperties({"images"}) // 이미지의 user 가 가지고 있는 images는 필요없으니 무시하도록 설정
	@JoinColumn(name="userId") // user 객체를 저장할 수 없음. 외래키가 저장됨. 외래키의 컬럼명을 설정
	@ManyToOne(fetch = FetchType.EAGER) // 이미지를 select하면 조인해서 User 정보를 같이 들고옴
	private User user; // 업로드 한 사람. 한 명은 여러 개의 이미지를 등록할 수 있다. (user 1 : image N)
	
	// 이미지 좋아요
	@JsonIgnoreProperties({"image"}) // Image 리턴할 때 likes 리턴하는데, likes에서 image는 무시하라는 의미
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;
	
	// 이미지 댓글
	@OrderBy("id DESC")
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	private LocalDateTime createDate;
	
	@Transient // DB에 컬럼이 만들어지지 않도록 하는 어노테이션
	private boolean likeState; // 로그인 유저의 해당 이미지 좋아요 여부
	
	@Transient
	private int likeCount; // 좋아요 수
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	// 오브젝트를 콘솔에 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함.
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl + ", createDate=" + createDate + "]";
//	}
	
}
