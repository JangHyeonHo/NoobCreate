package sec03.brd05;

import java.util.List;

//BoardDAO객체를 생성 한후 selectAllArticles()메소드를 호출해
//DB에 저장된 모든 글을 검색 해 가져오는 역할을 하는 서비스 관련 클래스 
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService() {//생성자
		boardDAO = new BoardDAO();//생성자 호출시..BoardDAO객체 생성 
	}
	
	//컨트롤러에서 modArticle메소드를 호출하면서
	//다시 BoardDAO의 updateArticle()메소드를 호출해 수정(UPDATE)할 데이터(ArticleVO객체)전달
	public void modArticle(ArticleVO article){
		
		boardDAO.updateArticle(article);
		
	}
	
	
	
	//새로운글을 DB에 추가시키기 위해 메소드 호출!
	//글추가에 성공하면 글번호를 반환해서 컨틀로러로 전달함.
	public int addArticle(ArticleVO articleVO){
		
		return  boardDAO.insertNewArticle(articleVO);
		
	}
	
	public List<ArticleVO> listArticles(){
		
		//BoardDAO객체의 selectAllArticles()메소드를 호출해 전체 글을 검색해서 가져옴
		List<ArticleVO>  articleList = boardDAO.selectAllArticles();
		
		return articleList; //BoardController로 리턴
	}


	//BoardController에서 호출한 메소드로 상세볼 글번호를 매개변수로 전달 받아....
	//BoardDAO의 selectArticle()메소드 호출시 인자로 전달하여 하나의 글정보 검색 요청을 함.
	//검색한 결과를 ArticleVO객체로 반환 받는다.
	//그후 BoardController로 다시~~ ArticleVO를 리턴 한다.
	public ArticleVO viewArticle(int articleNO) {
		
		//BoardDAO의 selectArticle()메소드 호출시 인자로 전달하여 하나의 글정보 검색 요청을 함.
		//검색한 결과를 ArticleVO객체로 반환 받는다.
		ArticleVO article = boardDAO.selectArticles(articleNO);
		
		return article;//그후 BoardController로 다시~~ ArticleVO를 리턴 한다.
	}	
	
	
}//BoardService클래스 끝
/*
 여기서 잠깐! 쉬어가기!
 BoardDAO클래스의 메소드 이름은 보통 각메소드들이 실행하는SQL문에 의해 결정 됩니다.
 예를 들어 selectAllArticles()메소드는 전체 글 정보를 조회하는 SQL문을 실행하므로
 메소드이름에 selectAll이 들어 갑니다.
 */


