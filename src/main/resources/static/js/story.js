/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

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

			<button>
				<i class="fas fa-heart active" id="storyLikeIcon-1" onclick="toggleLike()"></i>
			</button>
		</div>

		<span class="like"><b id="storyLikeCount-1">3 </b>likes</span>
		
		<div class="sl__item__contents__content">
			<p>${image.caption}</p>
		</div>
		
		<!-- 댓글 -->
		<div id="storyCommentList-1">
			<div class="sl__item__contents__comment" id="storyCommentItem-1"">
				<p>
					<b>Lovely :</b> 부럽습니다.
				</p>

				<button>
					<i class="fas fa-times"></i>
				</button>
			</div>
		</div>
		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="storyCommentInput-1" />
			<button type="button" onClick="addComment()">게시</button>
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
function toggleLike() {
	let likeIcon = $("#storyLikeIcon-1");
	if (likeIcon.hasClass("far")) {
		likeIcon.addClass("fas");
		likeIcon.addClass("active");
		likeIcon.removeClass("far");
	} else {
		likeIcon.removeClass("fas");
		likeIcon.removeClass("active");
		likeIcon.addClass("far");
	}
}

// (4) 댓글쓰기
function addComment() {

	let commentInput = $("#storyCommentInput-1");
	let commentList = $("#storyCommentList-1");

	let data = {
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-2""> 
			    <p>
			      <b>GilDong :</b>
			      댓글 샘플입니다.
			    </p>
			    <button><i class="fas fa-times"></i></button>
			  </div>
	`;
	commentList.prepend(content);
	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment() {

}







