package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	// 여러 곳에서 사용되는 경우, 
	// yml 파일에 file: path: 로 만들어 두고 해당 값을 가져와서 사용하는 것이 실수도 줄이고 효율적이다.
	@Value("${file.path}")
	private String uploadFolder; 
	
	@Transactional(readOnly=true) // readOnly=true : 영속성 컨텍스트 변경 감지를 해서 더티체킹, flush(반영)을 안 하게 함.
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		return images;
	}
	
	@Transactional // 
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		// 사용자가 업로드하는 파일의 파일명.확장자 가 중복될 수 있으므로, 구분하기 위해 UUID를 이용해보자.
		// UUID(Universally Unique IDentifier): 네트워크 상에서 고유성이 보장되는 id룰 만들기 위한 표준 규약.
		// 굉장히 낮은 확률로 UUID도 중복될 수가 있으니, UUID + 파일명 형태로 파일명을 생성하도록 한다.		
		UUID uuid = UUID.randomUUID(); 
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 실제 파일의 '파일명.확장자' 가져오기
		// System.out.println("이미지 파일 이름: "+imageFileName);
		
		// 실제 파일을 저장할 파일 경로 설정(yml에 설정한 폴더 경로 + 파일명)
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		// 통신, I/O 작업에는 예외가 발생할 수 있다. 런타임시에만 발생하는 에러이므로 try-catch 사용해서 잡아준다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); // 파일 업로드
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); // 생성한 파일 이름, 업도르 유저를 보내준다.
		Image imageEntity = imageRepository.save(image);		
		// System.out.println(imageEntity);
		// 위의 주석을 풀면 무한 참조 에러가 발생한다. 
		// JPA에서 imageEntity(=imageEntity.toString()) 을 출력하면 아래의 user 부분 때문에 무한 참조 발생. 주의하도록 하자.
		// 아래: Image.java 에서 generate toString 한 부분.
//		@Override
//		public String toString() {
//			return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl + ", user=" + user
//					+ ", createDate=" + createDate + "]";
//		}
		
		// 업로드 폴더를 프로젝트 외부에 두는 이유
		// 프로젝트 내 .java 파일은 1.서버 실행 시 2.컴파일 되어 3.target 폴더에 .class 형태로 들어가고 4. target 폴더 내부의 파일들을 실행하게 된다.
		// 서버 실행 시 컴파일 된 파일들과 정적인 일반 파일들(ex.img)은 target 폴더 내 들어가게 되는데 이를 deploy(배포)한다고 한다.
		// 결국 실행한다는 것은 target 폴더 내부를 실행하는 것이다.
		// 업로드 폴더가 프로젝트 내부에 있다면
		// 1.사진업로드 2.upload 3.target폴더로 들어가고(deploy) 4.실행
		// 정적 파일은 용량이 높아 .java파일보다 deploy 시간이 오래 걸린다.
		// 사진업로드 시 deploy 시간 차 때문에 파일이 타겟 폴더로 들어가기 전에 다음 작업이 실행될 수 있다.
		// (deploy 시간 보다 프로필 페이지로 이동하는 시간이 빠르다. 그럼 화면이 먼저 떠서 엑박이 뜰 수 있다!)
		// 업로드 폴더가 프로젝트 외부에 있다면
		// 업로드 후 deploy가 없으니 바로 프로필 페이지로 이동해도 엑박이 뜰 이유가 없음.
		
		
	}
}
