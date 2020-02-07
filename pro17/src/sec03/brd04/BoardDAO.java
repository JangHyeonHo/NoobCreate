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

//����
public class BoardDAO {

	private DataSource dataFactory;//Ŀ�ؼǵ��� ������ ���� ����
			Connection conn;
			PreparedStatement pstmt;
			ResultSet rs;
	public BoardDAO(){
		try{
			//InitialContext ��ü�� �ϴ� ��Ȱ�� ��Ĺ �����  context.xml�� ���ؤ� ������
			//Context ��ü�� ������ �ϴ� ��������.
			
			Context ctx = new InitialContext();
			
			
			//JDNI ������� �����ϱ� ���� �⺻���(java:/comp/env)�� �����մϴ�.
			//ȯ������ ���õ� ���ؽ�Ʈ ��ü�� �����ϱ� ���� �⺻ �ּ��Դϴ�.
			
			Context envConText = (Context)ctx.lookup("java:/comp/env");
			
			/*Ŀ�ؼ��� �ڿ� ���*/
			//�׷� �� �ٽ������� context.xml�� �� ������<Resource name = "jdbc/oracle"../>
			//�±��� name �Ӽ����� "jdbc/oracle�� �̿��� ���Ͽ� �̸� DB�� �����س���
			//Datasource��ü(Ŀ�ؼ�Ǯ ��Ȱ�� �ϴ� ��ü)�� �޾ƿɴϴ�.
			dataFactory = (DataSource)envConText.lookup("jdbc/oracle");
			
			
		}catch(Exception err){
			err.printStackTrace();
		}
	}//������
			
	//DB�� ������ �߰��ϱ����� DB�������ϴ� �����ֽ� �� ��ȣ�� �˻��ؼ� �����ϴ� �޼ҵ�
	private int getNewArticleNO(){
		try {
			conn = dataFactory.getConnection();
			//�۹�ȣ �߿��� ���� ū �۹�ȣ�� ��ȸ�ϴ� SQL��
			String query = "SELECT max(articleNO) from t_board";
			
			pstmt =conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				return(rs.getInt(1) +1);//���� �ֽ� �� ��ȣ�� 1�� ���� ��ȣ�� ��ȯ�� => ���� �� ��ȣ 
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
	
	
	//DB�� ���� ������ �߰��ϴ� �޼ҵ�
	//insertNewArticle �޼ҵ��� SQL ��(insert��)�� ���� �ϱ� ����
	//�ٷ����� getnewArticleNo() �޼ҵ带 ȣ���� DB�� �߰��� �� �� ��ȣ(�ֽű۹�ȣ +1)�����´�. 
	public int insertNewArticle(ArticleVO article){
		
		int articleNO = getNewArticleNO();// DB�� �߰��� ���۹�ȣ�� ����
		
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
	
	
	
	
	//BoardService Ŭ�������� BoardDAO �� selectAllArticles() �޼ҵ带 ȣ���ϸ�
	//������ SQL���� �̿��� ������ ������ ��ü ���� ��ȸ�� �� ��ȯ �մϴ�.
	public List<ArticleVO> selectAllArticles() {
		
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();//��ȸ�� �۵��� �����ϱ� ���� �뵵
		
		try {
			//Ŀ�ؼ� Ǯ���� Ŀ�ؼ� ���
			conn = dataFactory.getConnection();
			
			//������ ������ ��ü�� ��ȸ�ϴ� ����Ŭ�� ������ SQL��
			String query = "select LEVEL,articleNO,parentNO,title,content,id,writeDate"
						+ " from t_board"
						+ " START WITH parentNO=0"
						+ " CONNECT BY PRIOR articleNO=parentNO"
						+ " ORDER SIBLINGS by articleNO DESC";
		/*
		  	�� Select ���� ���� ����
		  	1. ���� START WITH parentNO=0
		  		-> parentNO�� ���� 0�� ���� �ֻ��� ���� �̴� ��� �ǹ�.
		  		   parentNO�� 0�̸� �� ���� �ֻ��� �θ���� �Ǵ°��Դϴ�.
		  	2. CONNECT BY PRIOR articleNO=parentNo
		  		-> ������ � �θ�۰� ����Ǵ��� ��Ÿ���ϴ�.
		  	3. ORDER SIBLINGS by articleNO DESC
		  		-> ���������� ��ȸ�� ���� articleNO ������������ �����Ͽ� �˻���.
		  	
		 */
			
		pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			int level = rs.getInt("level");//������ ����(����)����
			int articleNO = rs.getInt("articleNO");//�� ��ȣ
			int parentNO = rs.getInt("parentNO");//�θ�� ��ȣ
			String title= rs.getString("title");//����
			String content = rs.getString("content");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			//�˻��� �� ������ ���ڵ� ������ ������ ArticleVO��ü ���� �� �� ������ ����
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
				System.out.println("�ڿ����� �Ϸ�!");
				e.printStackTrace();
			}
		}
		
		return articlesList; // ����DAO -> �λ���Service���� ��ȯ�ϴ°�
	}//selectAllArticles()

	//BoardSerivce�κ��� ���޹��� �� ��ȣ�� �̿��� �������� ��ȸ��. ��ȸ �� ��ȯ��.
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
			
			//�˻��� �ϳ��� �� ������ ResultSet���� ����� ArticleVO��ü�� �� ������ ����
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

	
}//BoardDAO - ���� ��
