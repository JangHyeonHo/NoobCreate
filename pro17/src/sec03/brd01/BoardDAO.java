package sec03.brd01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			
	//BoardService Ŭ�������� BoardDAO �� selectAllArticles() �޼ҵ带 ȣ���ϸ�
	//������ SQL���� �̿��� ������ ������ ��ü ���� ��ȸ�� �� ��ȯ �մϴ�.
	public List<ArticleVO> selectAllArticles() {
		
		List articlesList = new ArrayList();//��ȸ�� �۵��� �����ϱ� ���� �뵵
		
		try {
			//Ŀ�ؼ� Ǯ���� Ŀ�ؼ� ���
			conn = dataFactory.getConnection();
			//������ ������ ��ü�� ��ȸ�ϴ� ����Ŭ�� ������ SQL��
			String query = "select level,articleNo,parentNO,title,content,id,writeDate"
						+ " from t_board"
						+ " start with parentNO=0"
						+ " CONNECT BY PRIOR articleNO=parentNo"
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
					rs.close();
				}
				if(conn != null){
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("�ڿ����� �Ϸ�!");
				e.printStackTrace();
			}
		}
		
		return articlesList; // ����DAO -> �λ���Service���� ��ȯ�ϴ°�
	}

	
}//BoardDAO - ���� ��
