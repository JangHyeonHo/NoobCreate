package sec03.brd02;

import java.util.List;

//부사장
//BoardDAO 객체를 생성한 후 selectAllArticles()메소드를 호출해 전체 글을 검색해서 가져옵니다.
public class BoardService {

	BoardDAO boardDAO;
	
	
	public BoardService(){
		boardDAO = new BoardDAO();
	}
	
	//BoardController에서 호출한 메소드로서
	//글쓰기 창에서 입력된 정보를 ArticleVO객체에 각 변수에 저장한 후 매개변수로 전달받아
	//다시 BoardDAO객체의 insertNewArticle()메소드를 호출하면서 추가할 새글 정보(ArticleVO)를 인자로 전달하여
	//DB에 INSERT작업을 명령하게됨.
	public void addArticle(ArticleVO articleVO){
		boardDAO.insertNewArticle(articleVO);
		
	}
	
	
	
	public List<ArticleVO> listArticles(){//BoardController사장님이 호출하는 메소드
		
		//부장인 BoardDAO의 selectAllArticles() 메소드 호출해 전체글을 검색해서(list를) 반환받음
		List<ArticleVO> articleList = boardDAO.selectAllArticles();
		
		return articleList;//검색한 전체 글 정보들(ArticleVO들)을 담고 있는 ArrayList 서블릿에 반환.
			// 사장Controller 에게 반환함.
		
		
	}
	
	
}
/*
여기서 잠깐! 쉬어가기!
BoardDAO클래스의 메소드 이름은 보통 각메소드들이 실행하는SQL문에 의해 결정 됩니다.
예를 들어 selectAllArticles()메소드는 전체 글 정보를 조회하는 SQL문을 실행하므로
메소드이름에 selectAll이 들어 갑니다.
*/
