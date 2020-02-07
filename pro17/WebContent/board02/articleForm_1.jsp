<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<%
	request.setCharacterEncoding("UTF-8");

%>
  
<c:set var="contextPath" value="${pageContext.request.contextPath }" />

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글쓰기 창</title>
	
	<script src="http://code.jqeury.com/jquery-latest.min.js"></script>
	
	<script type="text/javascript">
		
		//제이쿼리 이용해 아래쪽의 <input type="file"> 태그에서 이미지 파일 첨부시 미리보기 기능을 구현하는 함수
		function readURL(input){//<input type="file"...> 태그를 매개변수로 전달받음.
			
			// 크롬 웹브라우저의 F12개발자 모드로 가서 console탭에 띄운 로그 메세지를 확인한다
			console.debug(input);
			console.debug(input.files);
			
			//참고
			//<input type="file">인 태그 객체의 files메소드를 호출하면 fileList라는 배열이 생성되면서
			//Filelist 배열 내부의 0번째 인덱스 위치에 아래에서 선택한 (업로드할파일 this) 파일 정보들을 key:value값을 쌍으로 저장한 file객체가 저장되어 있음.
			//F12 들어가보면 파일이름이나 뭐 업로드 시간같은거 나오더라 신기
			
			//FileList 라는 배열이 존재하고
			// FileList라는 배열의 0번째 인덱스 위치에 아래에서 파일 업로드 하기 위해 선택한 File객체가
			// 저장되어 있다면 => 요약 : 업로드할 파일을 선택했다면? <input type="file"> 태그에서 업로드하기 위한 이미지 파일을 선택했다면 
			if(input.files && input.files[0]){
				//동적으로 <img>태그를 만들어서 추가함.
				$("#tdImg").html("<img id='preview' src='#' width=200 height=200/>");
				
				//파일내용을 읽어올 객체 생성
				var reader = new FileReader();
				
				// 지정한 img 태그에 첫번째 파일 input에 첨부한 파일에 대한 file객체를 읽어 드립니다.
				reader.readAsDataURL(input.files[0]);
				
				//파일을 모두 읽어 들였다면 
				reader.onload= function(ProgressEvent){
					//읽어 들인 File객체 정보는 매개변수로 넘어오는 ProgressEvent객체 내부의
					//target속성에 대응되는 객체(JSON 객체 데이터 형식)
					//또한 JSON 객체 데이터 내부에는 result속성에 읽어 들인 FIle객체 정보가 저장되어 있다.
					
					colsole.debug(ProgressEvent);
				}
				
			}		
			
			
			
			
		}
	</script>



</head>
<body>
	<h1 style="text-align: center;">새글 쓰기</h1>
	
	<form name="articleForm" action="${contextPath}/board/addArticle.do" method="post" enctype="multipart/form-data">
		<table border="0" align="center">
			<tr>
				<td align="right">글제목</td>
				<td colspan="2">
					<input type="text" size="67" maxlength="500" name="title"/>
				</td>
				
			</tr>
			<tr>
				<td align="right" valign="top"><br>글내용:</td>
				<td colspan="2">
					<textarea name="content" rows="10" cols="65" maxlength="4000"></textarea>
				</td>
			</tr>
			<tr>
				<td align="right">이미지 파일 첨부</td>
				<td>
					<%--첨부할 다른 이미지 파일을 선택해서 변화가 일어나면 readURL함수 호출시
						변화가 일어난 input태그 전달(this로 전달) --%>
					<input type="file" name="imageFileName" onchange="readURL(this);">
				</td>
				<td id="tdImg"></td>
			</tr>
			<tr>
				<td align="right"></td>
				<td colspan="2">
					<input type="submit" value="글쓰기">
					<input type="button" value="글목록보기" onclick="backToList(this.form)"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>