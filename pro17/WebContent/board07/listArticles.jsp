<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>    

<%--
	화면을 구현하는 JSP페이지인 listArticles.jsp를 다음과 같이 작성 합니다.
	전체글수(totArticles)가 100개를 넘는 경우, 100인경우, 100개를 넘지 않는 경우로 나누어 페이지 번호를 표시 하도록 구현함
	
	전체 글수가 100개가 넘지 않으면 전체글수를 10으로 나눈 몫에 1을 더한값이 페이지번호로 표시 됩니다.
	예를 들어  전체 글수가 13개이면 10으로 나누었을때의 몫인 1에  1을 더해 2페이지번호로 표시 됩니다.
	
	만약 전체 글수가 100개일때는 정확히 10개의 페이지가 표시되며,
	
	100개를 넘을 때는 다음 section으로 이동할수 있도록 마지막페이지번호 옆에 next를 표시 합니다.

 --%>    
    
<%--컨텍스트 패스 주소 얻기 --%>    
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>   
    
<%
	//BoardController.java서블릿으로 부터 응답할 DB로부터 조회한 모든 글정보 한글처리
	request.setCharacterEncoding("UTF-8");
%>    
    
<%-- HashMap으로 저장해서 넘어온 값들은 이름이 길어서 사용하기 불편함니다; <c:set>태그를 이용해서 각 값들을 짧은 변수이름으로 저장함니다. --%>    
    
<c:set var="articlesList" value = "${articlesMap.articlesList}"/>
<c:set var="totArticles" value = "${articlesMap.totArticles }" />    
<c:set var="section" value="${articlesMap.section }" />
<c:set var="pageNum" value="${articlesMap.pageNum }" />

    
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글목록창</title>.

	<style type="text/css">
		.cls1{text-decoration: none;}
		.cla2{text-align: center; font-size: 30px;}
		/*선택된 페이지 번호를 빨간색으로 표시함*/
		.sel_page{text-decoration: none; color : red;}
		.no-unline{text-decoration: none;}
		
	</style>

</head>
<body>
<table align="center" border="1" width="80%">
		<tr height="10" align="center" bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>글제목</td>
			<td>작성일</td>
		</tr>
<c:choose>
<%--BoardController.java서블릿으로 부터 전달 받은 request영역에 저장되어 
   articlesList속성으로 바인딩된 ArrayList객체가 저장되어 있지 않다면? --%>
	<c:when test="${articlesList == null }">
		<tr height="10">
			<td colspan="4">
				<p align="center">
					<b><span style="font-size: 9pt">등록된 글이 없습니다.</span></b>
				</p>
			</td>
		</tr>	
	</c:when>
	<%--request영역에 바인딩된 ArrayList가 존재 한다면(검색한 글이 존재 한다면) --%>
	<c:when test="${articlesList != null}">
		
<%--BoardController.java서블릿으로부터 전달 받은 request영역에 
    articlesList속성으로 바인딩된  
    ArrayList객체의 크기(검색한 글의 갯수(ArticleVO객체의 갯수))만큼 반복 하여
 	검색한 글정보(ArticleVO)들을 ArrayList객체 내부의 인덱스 위치로부터 차례대로 꺼내와서 
 	글목록을 표시 합니다.
 --%>
	<c:forEach var="article" items="${articlesList}" varStatus="articleNum">
		<tr align="center">
			<td width="5%">${articleNum.count}</td><%--varStatus의 count속성을 이용해 
			                                                                           글번호를 1부터 자동으로 표시합니다--%>
			<td width="10%">${article.id}</td> <%--ArticleVO객체(검색한 글 하나의 정보)의 
			                                       id변수값(작성자 id)출력 --%>
			<td align="left" width="35%">
				<%--왼쪽으로 30px만큼 여백을 준 후 글제목을 표시할 목적으로 여백을 줌 --%>
				<span style="padding-right: 30px"></span>
				<c:choose>
					<%--조건:<forEach>태그 반복시 각글의 level값이 1보다 크면? 
					              답변글(자식글)이므로 --%>
					<c:when test="${article.level > 1 }">
						<%--다시 내부 <forEach>태그를 이용해 1부터 level값까지 반복하면서
						    부모글 밑에 들여쓰기하여 답글(자식글)임을 표시함.--%>
						<c:forEach begin="1" end="${article.level}" step="1">
							<span style="padding-left: 20px"></span>
						</c:forEach>
						<%--공백 다음에 자식글을 표시함. --%>
						<span style="font-size: 12px">[답변]</span>
						<a class="cls1" href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">
							${article.title}
						</a>	
					</c:when>
					
					<%--조건 : 이때 level값이 1보다 크지 않으면 부모 글이므로 공백 없이 표시함. --%>
					<c:otherwise>
						<a  class="cls1" href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">
							${article.title}
						</a>
					</c:otherwise>
					
				</c:choose>
			</td>
			     <td width="10%">
					<fmt:formatDate value="${article.writeDate}"/>
				 </td>
		</tr>
	</c:forEach>
	</c:when>
</c:choose>
</table>
	<div class="cls2">
	<!-- 전체 글 수에 따라 페이징 표시를 다르게 함 -->
		<c:if test="${totArticles != null }">
			<c:choose>
				<!-- 글 개수라 100초과인 경우  -->
				<c:when test="${totArticles > 100}">
					<c:forEach var="page" begin ="1" end="10" step="1">
						<!-- 섹션값 2부터는 앞 섹션으로 이동할수있는 pre를 표시합니다. -->
						<c:if test="${section >1 && page==1 }">
							<a class="no-unline" href="${contextPath}/board/listArtices.do?section=${section-1}&pageNum=${(section-1)*10 +1}">
								&nbsp;pre
							</a>
						</c:if>
							<a class="no-unline" href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">
								${(section-1)*10+page}
							</a>
							<!-- 페이지 번호 10오른쪽에 다음섹션으로 이동할수있는 next를 표시합니다. -->
							<c:if test="${page ==10}">
								<a class="no-unline"
									href="${contextPath}/board/listArticles.do?section=${section+1}&pageNum=${section*10+1}">
									&nbsp;Next
								</a>
							</c:if>
					</c:forEach>
				</c:when>
				<!-- 등록된 글 개수가 100개인 경우 첫번째 섹션의 10개의 페이지만 표시하면된다. -->
				<c:when test="${totArticles == 100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<a class="no-unline" href="#">${page}</a>
					</c:forEach>
				</c:when>
				<!-- 등록된 글 개수가 100개 미만인 경우
					 전체 글개수가 100개보다 적을때 페이징을 표시함 -->
				<c:when test="${totArticles < 100}">
											<!-- 글 수가 100개가 되지 않음으로 표시되는 페이지는 10개가 되지않고, 전체 글수를 10으로 나누어 구한 값에 1을 더한 페이지까지 표시됩니다. -->
					<c:forEach var="page" begin="1" end="${totArticles/10+1}" step="1">
						<c:choose>
							<!-- 페이지 번호와 컨트롤러에서 넘어온 pageNum값이 같은경우 페이지 번호를 빨간색으로 표시하여 현재 사용자가 보고있는 페이지임을 알립니다. -->
							<c:when test="${page == pageNum }">
								<a class="sel-page" 
									href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">
									${page }
								</a>
							</c:when>
							<c:otherwise>
								<a class="no-unline" 
									href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">
									${page}
								</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>





	<a class="cls1" href="${contextPath}/board/articleForm.do">
		<p class="cls2">글쓰기</p>
	</a>
	
</body>
</html>











