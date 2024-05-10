// 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // form태그 액션을 막는다
	let data = $("#profileUpdate").serialize(); // 데이터를 key value 형태로 받아올 때 사용
	$.ajax({
		type: "PUT",
		url: '/api/user/'+userId,
		data: data,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json"
	}).done(res=>{ // done : HttpStatus 상태코드 200번대
		location.href= '/user/'+userId;
	}).fail(error=>{ // fail : HttpStatus 상태코드 200번대가 아닐 때
		if(error.data == null){
			alert(error.responseJSON.message);
		} else {
			alert(JSON.stringify(error.responseJSON.data));
		}
	});
}