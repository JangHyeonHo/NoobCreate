<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
    
<c:set var="contextPath" value ="${pageContext.request.contextPath }" />

<%
	request.setCharacterEncoding("UTF-8");
%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		//취소 버튼을 클릭하면
		//컨트롤러로 모든 글을 검색할수있게 요청함.
		function backToList(obj){
			obj.action="${contextPath}/board/listArticles.do";
			obj.submit();
		}
		
		//답글 작성시 이미지 파일 업로드를 위해서 이미지 미리보기 처리
      
	    //FilesList라는 배열이 존재 하고...
	    //FileList라는 배열의 0번째 인덱스 위치에 아래에서 파일을 업로드 하기 위해 선택한 File객체가 저장되어 있다면..
	    //요약 :아래의 <input type="file">태그에서 업로드를 하기 위한 파일을 선택 했다면?
		function readURL(input){
			if(input.files && input.files[0]){
				
				var reader= new FileReader();
				reader.onload = function(e){
					$('#preview').attr('src',e.target.result);
					
				}
				 //지정한 img태그에 첫번째 파일 input에 첨부한 파일에 대한 File객체를 읽어 들입니다.
			      reader.readAsDataURL(input.files[0]);
				
			}
		}
		
		
		
		
	</script>
</head>
<body>
	<h1 style="text-align: center;">답글 쓰기 </h1>
	<form name="frmReply"
	 	  method="post"
	  	  action="${contextPath}/board/addReply.do"
	  	  enctype="multipart/form-data">
		<table align="center">
			<tr>
				<td align="right">글쓴이:&nbsp;</td>
				<td><input type="text" size="5" value="Lee" disabled /></td>
			</tr>
			<tr>
				<td align="right">글제목:&nbsp;</td>
				<td><input type="text" size="67" maxlength="100" name="title" /></td>
			</tr>
			<tr>
				<td align="right" valign="top">글내용:&nbsp;</td>
				<td><textarea name="content" rows="10" cols="65" maxlength="4000"></textarea></td>
			</tr>
			<tr>
				<td align="right">이미지 파일 첨부: </td>
				<td><input type="file" name="imageFileName" onchange="readURL(this);"></td>
				<td><img id="preview" src="#" width="200" height="200" /></td>
			</tr>
			<tr>
				<td align="right"></td>
				<td> 
					<input type="submit" value="답글변경하기"/>
					<input type="button" value="취소 " onclick="backToList(this.form)" >
				</td>
			</tr>
		</table>
		
		
	</form>

</body>
</html>