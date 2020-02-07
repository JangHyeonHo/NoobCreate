package sec03.brd01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//서블릿 요청 주소 : /board/listArticles.do 주소로 DB에 저장된 전체글목록 검색 요청함.
//@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	
	BoardService boardService;
	ArticleVO articleVO;
	
	
	//서블릿 초기화시 BoardService객체를 생성 함.
	@Override
	public void init() throws ServletException {
		boardService = new BoardService();
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
		//뷰페이지 주소를 저장할 변수 선언
		String nextPage = "";
		
		//한글처리
		request.setCharacterEncoding("UTF-8");
		
		//요청 주소 받아 오긔  / 2단계 요청 주소 받아 오는게 getPathInfo
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		//listArticles.do
		
		try {
			List<ArticleVO> articlesList;
			if(action == null){//요청명이 null일떄
				
				//DB로 부터 모든 회원정보 검색 요청!(부사장 BoardService에게)
				articlesList = boardService.listArticles();
				//검색한 회원정보인 ArrayList를 request객체 메모리에 저장해서 유지
				request.setAttribute("articlesList", articlesList);
				//재요청할 뷰 이름을 nextPage변수에 저장.
				nextPage = "/board01/listArticles.jsp"; // V
				
			}else if(action.equals("/listArticles.do")){
				
				//DB로 부터 모든 회원정보 검색 요청!(부사장 BoardService에게)
				articlesList = boardService.listArticles();
				//검색한 회원정보인 ArrayList를 request객체 메모리에 저장해서 유지
				request.setAttribute("articlesList", articlesList);
				//재요청할 뷰 이름을 nextPage변수에 저장.
				nextPage = "/board01/listArticles.jsp"; // V
				
				
			}
			
			
			// 디스패치 방식으로 포워딩(재요청)
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			//재요청
			dispatcher.forward(request, response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	

	
	
}
