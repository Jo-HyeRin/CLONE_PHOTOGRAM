/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */
 
// 현재 로그인 한 사용자 아이디
let principalId = $("#principalId").val();

// (1) 스토리 로드하기
let page = 0;

function storyLoad() {
	$.ajax({
		url: `/api/image?page=${page}`,
		dataType: "json"
	}).done(res => {
//		console.log(res);
		res.data.content.forEach((image)=>{
			let storyItem = getStoryItem(image);
			$("#storyList").append(storyItem);
		});
	}).fail(error => {
		console.log(error);
	});
}
storyLoad();

function getStoryItem(image) {
	let item = `<div class="story-list__item">
	<div class="sl__item__header">
		<div>
			<img class="profile-image" src="/upload/${image.user.profileImageUrl}" onerror="this.src='/images/person.jpeg'" />
		</div>
		<div>${image.user.username}</div>
	</div>

	<div class="sl__item__img">
		<img src="/upload/${image.postImageUrl}" />
	</div>

	<div class="sl__item__contents">
		<div class="sl__item__contents__icon">

			<button> `;
			
	if(image.likeState){
		item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	} else {
		item += `<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	}
			
	item += `
			</button>
		</div>

		<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>
		
		<div class="sl__item__contents__content">
			<p>${image.caption}</p>
		</div>
		
		<!-- 댓글 -->
		<div id="storyCommentList-${image.id}">`;
		
		image.comments.forEach((comment) => {
			item += `
			<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
				<p>
					<b>${comment.user.username} :</b> ${comment.content}
				</p>`;
				
			if(principalId == comment.user.id){
				item += `
				<button onclick="deleteComment(${comment.id})">
					<i class="fas fa-times"></i>
				</button>
				`;
			}
			
				
				
			item += `	
			</div>
			`;
		});
		
			
		
	item += `
		</div>
		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
			<button type="button" onClick="addComment(${image.id})">게시</button>
		</div>
		<!-- // 댓글 -->

	</div>
</div>`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	// 사용자마다 화면의 크기가 다르다. 스크롤 하면 스크롤탑은 변해도 문서의 높이와 윈도우 높이는 변하지 않는다.	
//	console.log("윈도우 scrolllTop", $(window).scrollTop());
//	console.log("문서의 높이", $(document).height());
//	console.log("윈도우 높이", $(window).height());

	// 맨 밑으로 이동했을 경우 : "스크롤탑 = 문서의 높이 - 윈도우 높이" 가 된다.
	let checkNum = 	$(window).scrollTop() - ($(document).height() - $(window).height());
	
	// 페이지의 가장 아래인 경우 페이지 변수 값을 증가시키고 데이터 가져온다.
	if(checkNum < 1 && checkNum > -1){
		page++;
		storyLoad();
	}

});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);
	if (likeIcon.hasClass("far")) { // 좋아요 할 경우		
		$.ajax({
			type: "post",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {
			// 화면 좋아요 수 +1
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			// 화면 좋아요 하트 채우기
			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(error => {
			console.log("오류", error);
		});		
	} else { // 좋아요 취소 할 경우		
		$.ajax({
			type: "delete",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {			
			// 화면 좋아요 수 -1
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) - 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			// 화면 좋아요 하트 비우기
			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(error => {
			console.log("오류", error);
		});
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	// 댓글 내용 가져오기
	let data = { // 자바스크립트 데이터
		imageId: imageId,
		content: commentInput.val()
	}
	
	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}
	
	$.ajax({
		type: "post",
		url: "/api/comment",
		data: JSON.stringify(data), // 자바스크림트 데이터(data)를 json 데이터로 바꿔서 전달한다.
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(res => {		
		let comment = res.data;
		
		let content = `
		  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
		    <p>
		      <b>${comment.user.username} :</b>
		      ${comment.content}
		    </p>
		    <button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
		  </div>
		`;
		commentList.prepend(content);
	}).fail(error => {
		console.log("오류", error);
	});

	commentInput.val(""); // 인풋 필드를 비워준다.
}

// (5) 댓글 삭제
function deleteComment(commentId) {
	$.ajax({
		type: "delete",
		url: `/api/comment/${commentId}`,
		dateType: "json"
	}).done(res => {
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error => {
		console.log("오류", error);
	});
}