package sec03.brd08;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//BoardDAO객체를 생성 한후 selectAllArticles()메소드를 호출해
//DB에 저장된 모든 글을 검색 해 가져오는 역할을 하는 서비스 관련 클래스 
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService() {//생성자
		boardDAO = new BoardDAO();//생성자 호출시..BoardDAO객체 생성 
	}
	
	//답글 쓰기는 새글 쓰기와 동일 하게 BoardDAO 의 insertNewArticle()메소드를 이용함.
	public int addReply(ArticleVO article){
		
		//새글 추가시 사용한 메소드 재사용 하여 답글을 추가함.
		return boardDAO.insertNewArticle(article);
		
	}
	
	//BoardService클래스 에서는 페이징 기능이 필요한 글 목록과 전체 글 개수를 각각 조회할수 있도록 구현
	//HashMap를 생성한 후 조회한 두 정보를 각각 속성으로 저장.
	public Map listArticles(Map<String, Integer> pagingMap){
		Map articlesMap = new HashMap();
		
		//매개변수로 전달 받은 pageMap을 이용해 글 목록을 조회합니다.
		List<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
				
		//t_board테이블에 존재하는 전체 글수를 조회합니다.
		int totArticles= boardDAO.selectTotArticles();
		
		//조회한 글목록을 ArrayList에 저장한 후 다시 HashMap에 저장
		articlesMap.put("articlesList", articlesList);
		
		//조회한 전체 글 수를 HashMap에 저장
		articlesMap.put("totArticles", totArticles);
		
		return articlesMap;//참고 : JSP로 넘겨줘야할 정보가 많을 경우에는 각 request에 바인딩 해서 넘겨도 되지만
							// HashMap를 사용해 같은 종류의 정보를 묶어서 넘기면 편리합니다. 
		
		
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

	//BoardController에서 removeArticle()메소드 호출시 ..
	//매개변수로 articleNO로 글번호를 전달 받아... BoardDAO의 selectRemoveArticles()메소드를 
	//먼저 호출해  글번호에 대한 글과 그 자식글 글번호를 전달받아 ArrayList에  저장합니다.
	//그런 다음  deleteArticle()메소드를 호출해 글번호에 대한 글과 자식글을 삭제하고 글번호를 반환합니다.
	public List<Integer> removeArticle(int articleNO) {
		//글을 삭제하기 전 글번호들을 ArrayList객체에 저장합니다.
		List<Integer> articleNOList = boardDAO.selectRemoveArticles(articleNO);
		
		//삭제할 글번호를 전달해 글을 삭제 
		boardDAO.deleteArticle(articleNO);
		
		//삭제한 글번호 목록을 컨트롤러로 반환함
		return articleNOList;
	}
	
	
	
	
}//BoardService클래스 끝
/*
 여기서 잠깐! 쉬어가기!
 BoardDAO클래스의 메소드 이름은 보통 각메소드들이 실행하는SQL문에 의해 결정 됩니다.
 예를 들어 selectAllArticles()메소드는 전체 글 정보를 조회하는 SQL문을 실행하므로
 메소드이름에 selectAll이 들어 갑니다.
 */


