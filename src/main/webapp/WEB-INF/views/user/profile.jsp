<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<!--프로필 섹션-->
<section class="profile">
	<!--유저정보 컨테이너-->
	<div class="profileContainer">

		<!-- 유저이미지 -->
		<div class="profile-left">
			<div class="profile-img-wrap story-border" onclick="popup('.modal-image')">
				<form id="userProfileImageForm">
					<input type="file" name="profileImageFile" style="display: none;" id="userProfileImageInput" />
				</form>
				<img class="profile-image" src="/upload/${dto.user.profileImageUrl}" onerror="this.src='/images/person.jpeg'" id="userProfileImage" />
			</div>
		</div>
		<!-- // 유저이미지 -->

		<!--유저정보 및 사진등록 구독하기-->
		<div class="profile-right">
			<div class="name-group">
				<h2>${dto.user.name}</h2>
								
				<!-- 아래처럼 구현할 수도 있지만, 뷰 페이지에 자바 코드를 섞거나 연산을 하는 것은 추천하지 않는다. -->
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${principal.user.id == user.id}"> --%>
<!-- 						<button class="cta" onclick="location.href='/image/upload'">사진등록</button> -->
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<!-- 						<button class="cta" onclick="toggleSubscribe(this)">구독하기</button> -->
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
				
				<!-- 페이지 주인 여부를 데이터로 받아와서 아래처럼 구현해보자. -->
				<c:choose>
					<c:when test="${dto.pageOwnerState}">
						<button class="cta" onclick="location.href='/image/upload'">사진등록</button>
					</c:when>
					<c:otherwise>
					
						<!-- 구독하기&구독취소 -->
						<c:choose>
							<c:when test="${dto.subscribeState}">
								<button class="cta blue" onclick="toggleSubscribe(${dto.user.id}, this)">구독취소</button>
							</c:when>
							<c:otherwise>
								<button class="cta" onclick="toggleSubscribe(${dto.user.id}, this)">구독하기</button>
							</c:otherwise>							
						</c:choose>	
						<!-- // 구독하기&구독취소 -->	
										
					</c:otherwise>
				</c:choose>				
				<button class="modi" onclick="popup('.modal-info')">
					<i class="fas fa-cog"></i>
				</button>
			</div>

			<div class="subscribe">
				<ul>
					<li><a href=""> 게시물<span>${dto.imageCount}</span></a></li>
					<li><a href="javascript:subscribeInfoModalOpen(${dto.user.id});"> 구독정보<span>${dto.subscribeCount}</span></a></li><!-- 클릭 시 js 파일의 모달 오픈 함수 호출 -->
				</ul>
			</div>
			<div class="state">
				<h4>${dto.user.bio}</h4>
				<h4>${dto.user.website}</h4>
			</div>
		</div>
		<!--유저정보 및 사진등록 구독하기-->

	</div>
</section>

<!--게시물컨섹션-->
<section id="tab-content">
	<!--게시물컨컨테이너-->
	<div class="profileContainer">
		<!--그냥 감싸는 div (지우면이미지커짐)-->
		<div id="tab-1-content" class="tab-content-item show">
			<!--게시물컨 그리드배열-->
			<div class="tab-1-content-inner">
				<!--아이템들-->		
				<!-- JSTL EL표현식 에서 변수명을 적으면 get함수가 자동 호출된다.(LAZY일 경우 get 호출 중요!) -->	
				<!-- /upload/를 WebMvcConfig가 낚아채서 파일 경로 설정해준다. -->	
				<c:forEach var="image" items="${dto.user.images}"> 
					<div class="img-box">
						<a href=""> <img src="/upload/${image.postImageUrl}"/>
						</a>
						<div class="comment">
							<a href="#" class=""> <i class="fas fa-heart"></i><span>${image.likeCount}</span>
							</a>
						</div>
					</div>
				</c:forEach>
				<!--아이템들end-->
			</div>
		</div>
	</div>
</section>

<!--로그아웃, 회원정보변경 모달-->
<div class="modal-info" onclick="modalInfo()">
	<div class="modal">
		<button onclick="location.href='/user/1/update'">회원정보 변경</button>
		<button onclick="location.href='/logout'">로그아웃</button>
		<button onclick="closePopup('.modal-info')">취소</button>
	</div>
</div>
<!--로그아웃, 회원정보변경 모달 end-->

<!-- 프로필사진 바꾸기 모달(프로필 이미지 클릭 시 뜨는 모달) -->
<div class="modal-image" onclick="modalImage()">
	<div class="modal">
		<p>프로필 사진 바꾸기</p>
		<button onclick="profileImageUpload(${dto.user.id}, ${principal.user.id})">사진 업로드</button>
		<button onclick="closePopup('.modal-image')">취소</button>
	</div>
</div>

<!-- // 프로필사진 바꾸기 모달 -->



<!-- 구독정보 모달 -->
<div class="modal-subscribe">
	<div class="subscribe">
		<div class="subscribe-header">
			<span>구독정보</span>
			<button onclick="modalClose()">
				<i class="fas fa-times"></i>
			</button>
		</div>
		
		<div class="subscribe-list" id="subscribeModalList">
			<!-- 페이지 구독자 리스트 profile.js 에서 추가됨! -->
		</div>
	</div>

</div>
<!-- // 구독정보 모달 -->


<script src="/js/profile.js"></script>

<%@ include file="../layout/footer.jsp"%>