package sec03.brd04;

import java.util.List;

//부사장
//BoardDAO 객체를 생성한 후 selectAllArticles()메소드를 호출해 전체 글을 검색해서 가져옵니다.
public class BoardService {

	BoardDAO boardDAO;
	
	
	public BoardService(){
		boardDAO = new BoardDAO();
	}
	
	//새로운 글을 DB에 추가시키기위해 메소드 호출
	//글추가에 성공하면 글번호를 반환해서 컨트롤러로 전달함.
	
	public int addArticle(ArticleVO articleVO){
		
		return boardDAO.insertNewArticle(articleVO);
		
	}
	
	
	
	public List<ArticleVO> listArticles(){//BoardController사장님이 호출하는 메소드
		
		//부장인 BoardDAO의 selectAllArticles() 메소드 호출해 전체글을 검색해서(list를) 반환받음
		List<ArticleVO> articleList = boardDAO.selectAllArticles();
		
		return articleList;//검색한 전체 글 정보들(ArticleVO들)을 담고 있는 ArrayList 서블릿에 반환.
			// 사장Controller 에게 반환함.
		
		
	}

	//BoardController 에서 호출한 메소드로 상세볼 글번호를 매개변수로 전달받아
	
	
	public ArticleVO viewArticle(int articleNO) {
		
		//BoardDAO의 selectArticle()메소드호출시 인자로 전달하여 하나의 글정보 검색 요청을 함.
		//검색한 결과를 ArticleVO 객체로 반환받는다.
		ArticleVO article = boardDAO.selectArticles(articleNO);
		
		return article;//그 후 BoardController로 다시 ArticleVO를 리턴한다.
	
	}
	
	
}//BoardService 클래스 끗
/*
여기서 잠깐! 쉬어가기!
BoardDAO클래스의 메소드 이름은 보통 각메소드들이 실행하는SQL문에 의해 결정 됩니다.
예를 들어 selectAllArticles()메소드는 전체 글 정보를 조회하는 SQL문을 실행하므로
메소드이름에 selectAll이 들어 갑니다.
*/
