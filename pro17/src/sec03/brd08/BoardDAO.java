package sec03.brd08;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO { //DB�۾� ����Ͻ����� ó��
	
	private DataSource dataFactory;
			Connection conn;
			PreparedStatement pstmt;
	
	public BoardDAO() {//��ü ������ �����ڰ� ȣ��Ǹ鼭 Ŀ�ؼ�Ǯ ����
		try{
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// �Ű������� ���޹��� section���� pageNum�� �̿��ؼ� SQL ������ ��ȸ�մϴ�.
		public List selectAllArticles(Map pagingMap){
			
			List articlesList = new ArrayList();
			
			//���۵� section ���� pageNum���� �����ɴϴ�.
			int section = (Integer)pagingMap.get("section");
			int pageNum = (Integer)pagingMap.get("pageNum");
			
			try {
				conn = dataFactory.getConnection();
				String query = "SELECT * FROM ( " 
								+"select ROWNUM as recNum,"	+"LVL,"
								+"articleNO,"
								+"parentNO,"
								+"title,"
								+"id,"
								+"writeDate"
								+" from (select LEVEL as LVL,"
										+"articleNO," 
										+"parentNO," 
										+"title," 
										+"id,"
										+"writeDate" 
										+" from t_board"
										+" START WITH parentNO=0"
										+" CONNCECT BY PRIOR articleNO = parentNO"
										+" ORDER SIBLINGS BY articleNO DESC)" + ") "
								+" where recNum between(?-1)*100+(?-1)*10+1 and (?-1)*100+?*10";
				//section���� pageNum������ ���ڵ� ��ȣ�� ������ �������� ���մϴ�.
				//(�̸����� ���� 1�� ���۵Ǿ����� between 1 and 10�� �˴ϴ�.)
				System.out.println(query);
				
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, section);
				pstmt.setInt(2, pageNum);
				pstmt.setInt(3, section);
				pstmt.setInt(4, pageNum);
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					
					int level = rs.getInt("lvl");
					int articleNO = rs.getInt("articleNO");
					int parentNO = rs.getInt("parentNO");
					String title = rs.getString("title");
					String id = rs.getString("id");
					Date writeDate = rs.getDate("writeDate");
					
					ArticleVO article = new ArticleVO();
					article.setLevel(level);
					article.setArticleNO(articleNO);
					article.setParentNO(parentNO);
					article.setTitle(title);
					article.setId(id);
					article.setWriteDate(writeDate);
					
					articlesList.add(article);
					
				}//end while
				
				rs.close();
				pstmt.close();
				conn.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("selectAllArticles()�޼ҵ忡�� ���� : " + e);
			}
			
			return articlesList;
			
		}
	
	//���̺� �����ϴ� ��ü �� ���� ��ȸ
	public int selectTotArticles(){
		try {
			conn = dataFactory.getConnection();
			String query = "select count(articleNo) from t_board";//��ü �ۼ��� ��ȸ��.
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return (rs.getInt(1));
			}
			//�ڿ�����
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("selectTotArticles()�޼ҵ忡�� ���� : " + e);
		}
		return 0; //��ȸ�� �ۼ� = rs.getInt /�ƴϸ� 0�� ��ȯ�Ѵ� - 
	}
	
	
	
	
	
	
	//�Ű������� ���� �޴� ������ �۹�ȣ�� �ش�Ǵ� �۰� ���ڽı��� ���� 
	public void deleteArticle(int articleNO){		
		try {			
			conn = dataFactory.getConnection();
			String query = "DELETE FROM t_board ";
			       query += " WHERE articleNO in (";
			       query += " SELECT articleNO FROM t_board ";
			       query += " START WITH articleNO = ?";
			       query += " CONNECT BY PRIOR articleNO = parentNO )";				
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();//����			
			pstmt.close();
			conn.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	

	//BoardServiceŬ������ ���� ������ ������(ArticleVO��ü)�� �Ű� ������ ���� �޾�..
	//���޵� ���� �����Ϳ� ���� �̹��� ������ ���� �ϴ� ����  �̹��������� ���� ���� �ʴ� ��츦 ������
	//�������� SQL���� �����Ͽ� ����!!!!!!!
	public void updateArticle(ArticleVO article){
		
		int articleNO = article.getArticleNO();//getter�޼ҵ� ȣ�� �� ������ �۹�ȣ ���
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		try {
			conn = dataFactory.getConnection();
			String query = "update t_board set title=?, content=?";
			
			if(imageFileName != null && imageFileName.length() != 0){
				
				query += ",imageFileName=?";
			
			}	
			query += " where articleNO=?";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			
			//�̹��� ������ ���� �ϴ� ���� �׷��� �ʴ� ��츦 �����ؼ� ?�� ���� ��.
			if(imageFileName != null && imageFileName.length() != 0){
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			}else{
				pstmt.setInt(3, articleNO);
			}		
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//updateArticle�޼ҵ� ��
	
	//DB�� ������ �߰� �ϱ� ���� DB�� ���� �ϴ� ���� �ֽ� �۹�ȣ�� �˻��ؼ� ���� �ϴ� �޼ҵ�
	private int getNewArticleNO(){
		try {
			conn = dataFactory.getConnection();
			//�۹�ȣ�� ���� ū �۹�ȣ�� ��ȸ �ϴ� SQL��
			String query = "SELECT max(articleNO) from t_board";
			
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){//�ֽű۹�ȣ�� �˻��Ǿ��ٸ�
				return (rs.getInt(1) + 1);//���� ū �۹�ȣ�� 1�� ���� ��ȣ�� ��ȯ�� : ����-> ���۱۹�ȣ�� ���� �ϱ� ����
			}
			//�ڿ�����
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	//DB�� ���������� �߰��ϴ� �޼ҵ�
	//insertNewArticle�޼ҵ��� SQL��(insert��)�� �����ϱ� ����..
	//�ٷ����� getNewArticleNO()�޼ҵ带 ȣ����.. DB�� �߰��� ���۹�ȣ(�ֽű۹�ȣ  + 1)�� ��� �´�.
	public int insertNewArticle(ArticleVO article){
		
		int articleNO = getNewArticleNO(); //DB�� �߰��� ���۹�ȣ�� ����
		
		try {
			conn = dataFactory.getConnection();
			
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			
			String query = "INSERT INTO t_board (articleNO, parentNO, title, "
					     + "content, imageFileName, id)"
						 + " VALUES(?,?,?,?,?,?)";
		
			System.out.println(query);
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
		
		return articleNO; //������ �߰��ϰ� �߰��� ���۹�ȣ�� ����
	}
	
	
	
	
	//BoardServiceŬ�������� BoardDAO��  selectAllArticles()�޼ҵ带 ȣ���ϸ�
	//������ SQL���� �̿��� ������ ������ ��ü���� ��ȸ�� �� ��ȯ�մϴ�.
	public List selectAllArticles(){
		
		List articlesList = new ArrayList();
		
		try{
			//Ŀ�ؼ�Ǯ�κ��� Ŀ�ؼǰ�ü ���
			conn = dataFactory.getConnection();
			
			//������ ������ ��ü ���� ��ȸ�ϴ� ����Ŭ�� ������SQL��
			String query = "SELECT LEVEL,articleNO,parentNO,title,content,id,writeDate"
					     + " from t_board"
					     + " START WITH parentNO=0"
					     + " CONNECT BY PRIOR articleNO=parentNO"
					     + " ORDER SIBLINGS BY articleNO DESC";
			/*
			 �� SELECT���� ���� ����
			 
			 1. ���� START WITH parentNO=0
			    -> parentNO�� ���� 0�� ���� �ֻ��� �����̴ٶ�� �ǹ��Դϴ�.
			    -> parentNo�� 0�̸� �ױ��� �ֻ��� �θ���� �Ǵ� ���Դϴ�.
			 
			 2. CONNECT BY PRIOR articleNO=parentNO
			  	-> �� ���� � �θ�۰� ����Ǵ����� ��Ÿ���ϴ�.
			 
			 3. ORDER SIBLINGS BY articleNO DESC
			   -> ���� ������ ��ȸ�� ���� articleNo������ ������������ ���� �Ͽ� �ٽ� �˻�
			 
			 4. select���� LEVEL�����÷���  ������ SQL�� �����,
			    CONNECT BY PRIOR articleNO=parentNO�� ���� �����Ǹ鼭 �� ���� ���̸� ��Ÿ���ϴ�.
			       ����Ŭ���� �˾Ƽ� �θ�ۿ� ���ؼ� ���̸� ����ؼ� LEVEL�� ��ȯ �մϴ�.   
			 
			 5. ���� ������ SQL���� �����ϸ鼭 ����Ŭ�� ��ü �ۿ� ���ؼ� ���������� ��� ���� articleNo������,
			    �ٸ��۵��� parentNo�� ���ؼ� ������ �� �۵��� parentNo�� �� �Ʒ� ��������,
			  articleNo�� ���� �������� ���� �ϴ� ������ ��Ĩ�ϴ�.   
			     
			 */
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("level");//�� ���� ����(����)�� level������ ����
				int articleNO = rs.getInt("articleNO");//�۹�ȣ�� �������̹Ƿ� getInt()�� ���� ������
				int parentNO = rs.getInt("parentNO");//�θ�۹�ȣ
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				//�˻��� �ϳ��� �������� ArticleVO��ü�� �������� ������.
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				
				//ArrayList�� ArticleVO��ü �߰�
				articlesList.add(article);
			}//while��
			//�ڿ�����
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		//ArryList��ȯ
		return articlesList;
		
	}//selectAllArticles()


	//BoardService�κ��� ���� ���� �۹�ȣ�� �̿��� �������� ��ȸ��. ��ȸ�� ArticleVO�� ��ȯ��.
	public ArticleVO selectArticles(int articleNO) {
		
		ArticleVO article = new ArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			String query = "select articleNO, parentNO, title, content, "
					     + "imageFileName, id, writeDate"
					     + " from t_board where articleNO=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			int _articleNO = rs.getInt("articleNO");
			int parentNO = rs.getInt("parentNO");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String imageFileName = rs.getString("imageFileName");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			
			//�˻��� �ϳ��� �������� ResultSet���� ����� ArticleVO��ü�� �������� ����
			article.setArticleNO(rs.getInt("articleNO"));
			article.setParentNO(rs.getInt("parentNO"));
			article.setTitle(rs.getString("title"));
			article.setContent(rs.getString("content"));
			article.setImageFileName(rs.getString("imageFileName"));
			article.setId(rs.getString("id"));
			article.setWriteDate(rs.getDate("writeDate"));			
			//�ڿ�����
			rs.close();
			pstmt.close();
			conn.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return article;
	}

	//������ �ۿ� ���� �۹�ȣ�� ���� �ϴ� �޼ҵ� 
	public List<Integer> selectRemoveArticles(int articleNO) {
	
		List<Integer> articleNOList = new ArrayList<Integer>();	
		
		try {
			conn = dataFactory.getConnection();
			//������ �۵��� articleNO�� ��ȸ��.
			String query = "SELECT articleNO FROM t_board  ";
				   query += " START WITH articleNO=?";
				   query += " CONNECT BY PRIOR articleNO=parentNO";
			
		    pstmt = conn.prepareStatement(query);
		    pstmt.setInt(1, articleNO);
		    ResultSet rs = pstmt.executeQuery();
		    
		    while (rs.next()) {
				articleNO = rs.getInt("articleNo");
				articleNOList.add(articleNO);
			}
		    pstmt.close();
		    conn.close();
				   
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNOList;//BoardService�� ���� 
	}
	
}//BoardDAO







