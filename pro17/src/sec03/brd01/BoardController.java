package sec03.brd01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//���� ��û �ּ� : /board/listArticles.do �ּҷ� DB�� ����� ��ü�۸�� �˻� ��û��.
//@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	
	BoardService boardService;
	ArticleVO articleVO;
	
	
	//���� �ʱ�ȭ�� BoardService��ü�� ���� ��.
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
		//�������� �ּҸ� ������ ���� ����
		String nextPage = "";
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//��û �ּ� �޾� ����  / 2�ܰ� ��û �ּ� �޾� ���°� getPathInfo
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		//listArticles.do
		
		try {
			List<ArticleVO> articlesList;
			if(action == null){//��û���� null�ϋ�
				
				//DB�� ���� ��� ȸ������ �˻� ��û!(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				//�˻��� ȸ�������� ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				//���û�� �� �̸��� nextPage������ ����.
				nextPage = "/board01/listArticles.jsp"; // V
				
			}else if(action.equals("/listArticles.do")){
				
				//DB�� ���� ��� ȸ������ �˻� ��û!(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				//�˻��� ȸ�������� ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				//���û�� �� �̸��� nextPage������ ����.
				nextPage = "/board01/listArticles.jsp"; // V
				
				
			}
			
			
			// ����ġ ������� ������(���û)
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			//���û
			dispatcher.forward(request, response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	

	
	
}
