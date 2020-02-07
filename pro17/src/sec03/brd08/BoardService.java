package sec03.brd08;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//BoardDAO��ü�� ���� ���� selectAllArticles()�޼ҵ带 ȣ����
//DB�� ����� ��� ���� �˻� �� �������� ������ �ϴ� ���� ���� Ŭ���� 
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService() {//������
		boardDAO = new BoardDAO();//������ ȣ���..BoardDAO��ü ���� 
	}
	
	//��� ����� ���� ����� ���� �ϰ� BoardDAO �� insertNewArticle()�޼ҵ带 �̿���.
	public int addReply(ArticleVO article){
		
		//���� �߰��� ����� �޼ҵ� ���� �Ͽ� ����� �߰���.
		return boardDAO.insertNewArticle(article);
		
	}
	
	//BoardServiceŬ���� ������ ����¡ ����� �ʿ��� �� ��ϰ� ��ü �� ������ ���� ��ȸ�Ҽ� �ֵ��� ����
	//HashMap�� ������ �� ��ȸ�� �� ������ ���� �Ӽ����� ����.
	public Map listArticles(Map<String, Integer> pagingMap){
		Map articlesMap = new HashMap();
		
		//�Ű������� ���� ���� pageMap�� �̿��� �� ����� ��ȸ�մϴ�.
		List<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
				
		//t_board���̺� �����ϴ� ��ü �ۼ��� ��ȸ�մϴ�.
		int totArticles= boardDAO.selectTotArticles();
		
		//��ȸ�� �۸���� ArrayList�� ������ �� �ٽ� HashMap�� ����
		articlesMap.put("articlesList", articlesList);
		
		//��ȸ�� ��ü �� ���� HashMap�� ����
		articlesMap.put("totArticles", totArticles);
		
		return articlesMap;//���� : JSP�� �Ѱ������ ������ ���� ��쿡�� �� request�� ���ε� �ؼ� �Ѱܵ� ������
							// HashMap�� ����� ���� ������ ������ ��� �ѱ�� ���մϴ�. 
		
		
	}
	
	
	
	//��Ʈ�ѷ����� modArticle�޼ҵ带 ȣ���ϸ鼭
	//�ٽ� BoardDAO�� updateArticle()�޼ҵ带 ȣ���� ����(UPDATE)�� ������(ArticleVO��ü)����
	public void modArticle(ArticleVO article){
		
		boardDAO.updateArticle(article);
		
	}
	
	
	
	//���ο���� DB�� �߰���Ű�� ���� �޼ҵ� ȣ��!
	//���߰��� �����ϸ� �۹�ȣ�� ��ȯ�ؼ� ��Ʋ�η��� ������.
	public int addArticle(ArticleVO articleVO){
		
		return  boardDAO.insertNewArticle(articleVO);
		
	}
	
	public List<ArticleVO> listArticles(){
		
		//BoardDAO��ü�� selectAllArticles()�޼ҵ带 ȣ���� ��ü ���� �˻��ؼ� ������
		List<ArticleVO>  articleList = boardDAO.selectAllArticles();
		
		return articleList; //BoardController�� ����
	}


	//BoardController���� ȣ���� �޼ҵ�� �󼼺� �۹�ȣ�� �Ű������� ���� �޾�....
	//BoardDAO�� selectArticle()�޼ҵ� ȣ��� ���ڷ� �����Ͽ� �ϳ��� ������ �˻� ��û�� ��.
	//�˻��� ����� ArticleVO��ü�� ��ȯ �޴´�.
	//���� BoardController�� �ٽ�~~ ArticleVO�� ���� �Ѵ�.
	public ArticleVO viewArticle(int articleNO) {
		
		//BoardDAO�� selectArticle()�޼ҵ� ȣ��� ���ڷ� �����Ͽ� �ϳ��� ������ �˻� ��û�� ��.
		//�˻��� ����� ArticleVO��ü�� ��ȯ �޴´�.
		ArticleVO article = boardDAO.selectArticles(articleNO);
		
		return article;//���� BoardController�� �ٽ�~~ ArticleVO�� ���� �Ѵ�.
	}

	//BoardController���� removeArticle()�޼ҵ� ȣ��� ..
	//�Ű������� articleNO�� �۹�ȣ�� ���� �޾�... BoardDAO�� selectRemoveArticles()�޼ҵ带 
	//���� ȣ����  �۹�ȣ�� ���� �۰� �� �ڽı� �۹�ȣ�� ���޹޾� ArrayList��  �����մϴ�.
	//�׷� ����  deleteArticle()�޼ҵ带 ȣ���� �۹�ȣ�� ���� �۰� �ڽı��� �����ϰ� �۹�ȣ�� ��ȯ�մϴ�.
	public List<Integer> removeArticle(int articleNO) {
		//���� �����ϱ� �� �۹�ȣ���� ArrayList��ü�� �����մϴ�.
		List<Integer> articleNOList = boardDAO.selectRemoveArticles(articleNO);
		
		//������ �۹�ȣ�� ������ ���� ���� 
		boardDAO.deleteArticle(articleNO);
		
		//������ �۹�ȣ ����� ��Ʈ�ѷ��� ��ȯ��
		return articleNOList;
	}
	
	
	
	
}//BoardServiceŬ���� ��
/*
 ���⼭ ���! �����!
 BoardDAOŬ������ �޼ҵ� �̸��� ���� ���޼ҵ���� �����ϴ�SQL���� ���� ���� �˴ϴ�.
 ���� ��� selectAllArticles()�޼ҵ�� ��ü �� ������ ��ȸ�ϴ� SQL���� �����ϹǷ�
 �޼ҵ��̸��� selectAll�� ��� ���ϴ�.
 */


