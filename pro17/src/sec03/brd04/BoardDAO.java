package sec03.brd04;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//부장
public class BoardDAO {

	private DataSource dataFactory;//커넥션들을 저장할 변수 선언
			Connection conn;
			PreparedStatement pstmt;
			ResultSet rs;
	public BoardDAO(){
		try{
			//InitialContext 객체가 하는 역활은 톰캣 실행시  context.xml에 의해ㅓ 생성된
			//Context 객체에 접근을 하는 역할을함.
			
			Context ctx = new InitialContext();
			
			
			//JDNI 방법으로 접근하기 위해 기본경로(java:/comp/env)를 지정합니다.
			//환설정에 관련된 컨텍스트 객체에 접근하기 위한 기본 주소입니다.
			
			Context envConText = (Context)ctx.lookup("java:/comp/env");
			
			/*커넥션플 자원 얻기*/
			//그런 후 다시톰켓은 context.xml에 그 설정한<Resource name = "jdbc/oracle"../>
			//태그의 name 속성값인 "jdbc/oracle를 이용해 톰켓에 미리 DB에 연결해놓은
			//Datasource객체(커넥션풀 역활을 하는 객체)를 받아옵니다.
			dataFactory = (DataSource)envConText.lookup("jdbc/oracle");
			
			
		}catch(Exception err){
			err.printStackTrace();
		}
	}//생성자
			
	//DB에 새글을 추가하기위해 DB에존재하는 가장최신 글 번호를 검색해서 제공하는 메소드
	private int getNewArticleNO(){
		try {
			conn = dataFactory.getConnection();
			//글번호 중에서 가장 큰 글번호를 조회하는 SQL문
			String query = "SELECT max(articleNO) from t_board";
			
			pstmt =conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				return(rs.getInt(1) +1);//가장 최신 글 번호에 1을 더한 번호를 반환함 => 새글 글 번호 
			}
			//자원해제
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	//DB에 새글 정보를 추가하는 메소드
	//insertNewArticle 메소드의 SQL 문(insert문)을 실행 하기 전에
	//바로위의 getnewArticleNo() 메소드를 호출해 DB에 추가할 새 글 번호(최신글번호 +1)를얻어온다. 
	public int insertNewArticle(ArticleVO article){
		
		int articleNO = getNewArticleNO();// DB에 추가할 새글번호를 얻어옴
		
		try {
			conn = dataFactory.getConnection();
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			
			String query = "insert into t_board (articleNO,parentNO,title,content,imageFileName,id) "
							+"VALUES(?,?,?,?,?,?)";
			
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleNO;
		
		
		
	}
	
	
	
	
	//BoardService 클래스에서 BoardDAO 의 selectAllArticles() 메소드를 호출하면
	//계층형 SQL문을 이용해 계층형 구조로 전체 글을 조회한 후 반환 합니다.
	public List<ArticleVO> selectAllArticles() {
		
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();//조회한 글들을 저장하기 위한 용도
		
		try {
			//커넥션 풀에서 커넥션 얻기
			conn = dataFactory.getConnection();
			
			//계층형 구조로 전체글 조회하는 오라클의 계층형 SQL문
			String query = "select LEVEL,articleNO,parentNO,title,content,id,writeDate"
						+ " from t_board"
						+ " START WITH parentNO=0"
						+ " CONNECT BY PRIOR articleNO=parentNO"
						+ " ORDER SIBLINGS by articleNO DESC";
		/*
		  	위 Select 구문 참고 설명
		  	1. 먼저 START WITH parentNO=0
		  		-> parentNO의 값이 0인 글이 최상위 계층 이다 라는 의미.
		  		   parentNO가 0이면 그 글은 최상위 부모글이 되는것입니다.
		  	2. CONNECT BY PRIOR articleNO=parentNo
		  		-> 각글이 어떤 부모글과 연결되는지 나타냅니다.
		  	3. ORDER SIBLINGS by articleNO DESC
		  		-> 계층구조로 조회된 글을 articleNO 내림차순으로 정렬하여 검색함.
		  	
		 */
			
		pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			int level = rs.getInt("level");//각글의 깊이(계층)저장
			int articleNO = rs.getInt("articleNO");//글 번호
			int parentNO = rs.getInt("parentNO");//부모글 번호
			String title= rs.getString("title");//제목
			String content = rs.getString("content");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			//검색한 글 정보를 레코드 단위로 저장할 ArticleVO객체 생성 후 각 변수에 저장
			ArticleVO article = new ArticleVO();
			article.setLevel(level);
			article.setArticleNO(articleNO);
			article.setParentNO(parentNO);
			article.setTitle(title);
			article.setContent(content);
			article.setId(id);
			article.setWriteDate(writeDate);
			
			//ArrayList에 ArticleVO객체 추가
			articlesList.add(article);
			
		}//while
			
			
			
		} catch (Exception e) {
			System.out.println("selectAllArticles() err : " + e);
		}finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				System.out.println("자원해제 완료!");
				e.printStackTrace();
			}
		}
		
		return articlesList; // 부장DAO -> 부사장Service한태 반환하는거
	}//selectAllArticles()

	//BoardSerivce로부터 전달받은 글 번호를 이용해 글정보를 조회함. 조회 후 반환함.
	public ArticleVO selectArticles(int articleNO) {
		
		ArticleVO article = new ArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String query = "select articleNO, parentNO, title, content, imageFileName, id, writeDate"
							+ " from t_board where articleNO=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			//검색한 하나의 글 정보를 ResultSet에서 꺼내어서 ArticleVO객체의 각 변수에 저장
			article.setArticleNO(rs.getInt("articleNO"));
			article.setParentNO(rs.getInt("parentNO"));
			article.setTitle(rs.getString("title"));
			article.setContent(rs.getString("content"));
			article.setImageFileName(rs.getString("imageFileName"));
			article.setId(rs.getString("id"));
			article.setWriteDate(rs.getDate("writeDate"));
			
			//자원해제
			rs.close();
			pstmt.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return article;
	}

	
}//BoardDAO - 부장 ㅜ
