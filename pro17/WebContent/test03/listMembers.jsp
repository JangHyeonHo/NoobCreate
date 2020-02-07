<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix ="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  
<%
	request.setCharacterEncoding("UTF-8");
%>  
	<%--contextpath: http://localhost:8090/pro17 까지의 경로 --%>
	<c:set var="contextPath" value="${pageContext.request.contextPath }"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MemberController서블릿에서 request에 바인딩 한 ArrayList를 얻어 현재 화면에 출력</title>
	<style type="text/css">
		.cls1 {
			font-size: 40px;
			text-align: center;
		}
	</style>
	
	<c:choose>
		<%--회원 수정 작업 후 컨트롤러에서 넘긴 msg 값에 따라 작업 결과를 alert창에 출력 --%>
		<c:when test='${msg == "modified" }'>
			<script type="text/javascript">
				window.onload = function(){
					alert("회원정보를 수정했습니다.")
				}
			</script>
		</c:when>
		
		<%--회원 수정 작업 후 컨트롤러에서 넘긴 msg 값에 따라 작업 결과를 alert창에 출력 --%>
		<c:when test='${msg == "addmember" }'>
			<script type="text/javascript">
				window.onload = function(){
					alert("회원정보를 등록했습니다.")
				}
			</script>
		</c:when>
		
		<c:when test='${requestScope.msg == "deleted" }'>
			<script type="text/javascript">
				window.onload = function(){
					alert("회원 정보를 삭제 했습니다.");
				}
			</script>
		
		
		</c:when>
		
		
	</c:choose>

</head>
<body>
	<p class="cls1">회원정보</p>
	<table align="center" border="1">
		<tr align="center" bgcolor="lightgreen">
			<td width="7%"><b>아이디</b></td>
			<td width="7%"><b>비밀번호</b></td>
			<td width="7%"><b>이름</b></td>
			<td width="7%"><b>이메일</b></td>
			<td width="7%"><b>가입일</b></td>
			<td width="7%"><b>수정</b></td>
			<td width="7%"><b>삭제</b></td>
		</tr>
	<c:choose>
		<c:when test="${requestScope.membersList == null}">
			<tr>
				<td colspan="5">
					<b>등록된 회원이 없습니다.</b>
				</td>	
			</tr>
		</c:when>
		<c:when test="${requestScope.membersList != null}">
			<c:forEach var="membervo" items="${requestScope.membersList}">
				<tr align="center" bgcolor="lightgreen">
					<td>${membervo.id}</td>
					<td>${membervo.pwd}</td>
					<td>${membervo.name}</td>
					<td>${membervo.email}</td>
					<td>${membervo.joinDate}</td>
					<td>
						<a href="${contextPath}/member/modMemberForm.do?id=${membervo.id}">수정</a>
					</td>
					<td>
						<a href="${contextPath}/member/delMember.do?id=${membervo.id}">삭제</a>
					</td>
					
				</tr>
			</c:forEach>
		</c:when>
	</c:choose>
	</table>
	<a href="${pageContext.request.contextPath}/member/memberForm.do">회원가입 하긔</a>

</body>
</html>