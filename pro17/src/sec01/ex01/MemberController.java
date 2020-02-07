package sec01.ex01;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//클라이언트가 웹 브라우저 주소 창에 주소를 입력 하여 서블렛에게 요청합니다.
// 요청주제 : DB에 존재하는 모든 회원정보를 검색해줘
// 요청주소 : http://localhost:8090/pro17/mem.do

//@WebServlet("/mem.do")
public class MemberController extends HttpServlet{
	
	MemberDAO memberDAO;
	
	//init() 메소드에서 MemberDAO객체를 생성해서 초기화
	
	@Override
	public void init() throws ServletException{
		memberDAO = new MemberDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//한글처리
		request.setCharacterEncoding("UTF-8");
		
		//MemberDAO의 listMembers() 메소드를 호출하여 회원정보 조회 요청에 대해
		//DB로부터 검색한 회원정보를 ArrayList로 반환
		ArrayList membersList =memberDAO.listMembers();
		
		//이때 DB로부터 검색한 회원정보들(ArrayList에 담긴 MemberVO객체들)을 View페이지로 
		//전달하기위해 임시 저장 공간인 request내장객체 영역에 저장(바인딩)함.
		//request.setAttribute("속성명(키)", "응답값ArrayList->membersList");
		request.setAttribute("memberList", membersList);
		
		//그런 다음 RequestDispatcher 클래스를 이용해 회원 목록창(listMembers.jsp View페이지)로
		// 포워딩(재요청) 함
		RequestDispatcher dispatche = request.getRequestDispatcher("/test01/listMembers.jsp");
		dispatche.forward(request, response);
		
		//<jsp:forward page="/test01/listMembers.jsp"> 이거랑 동일한 기능입니다. 근대 저건 jsp지 여기 java에선 못써
		
	}
	
	
}
