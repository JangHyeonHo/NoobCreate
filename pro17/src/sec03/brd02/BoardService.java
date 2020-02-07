package sec03.brd02;

import java.util.List;

//�λ���
//BoardDAO ��ü�� ������ �� selectAllArticles()�޼ҵ带 ȣ���� ��ü ���� �˻��ؼ� �����ɴϴ�.
public class BoardService {

	BoardDAO boardDAO;
	
	
	public BoardService(){
		boardDAO = new BoardDAO();
	}
	
	//BoardController���� ȣ���� �޼ҵ�μ�
	//�۾��� â���� �Էµ� ������ ArticleVO��ü�� �� ������ ������ �� �Ű������� ���޹޾�
	//�ٽ� BoardDAO��ü�� insertNewArticle()�޼ҵ带 ȣ���ϸ鼭 �߰��� ���� ����(ArticleVO)�� ���ڷ� �����Ͽ�
	//DB�� INSERT�۾��� ����ϰԵ�.
	public void addArticle(ArticleVO articleVO){
		boardDAO.insertNewArticle(articleVO);
		
	}
	
	
	
	public List<ArticleVO> listArticles(){//BoardController������� ȣ���ϴ� �޼ҵ�
		
		//������ BoardDAO�� selectAllArticles() �޼ҵ� ȣ���� ��ü���� �˻��ؼ�(list��) ��ȯ����
		List<ArticleVO> articleList = boardDAO.selectAllArticles();
		
		return articleList;//�˻��� ��ü �� ������(ArticleVO��)�� ��� �ִ� ArrayList ������ ��ȯ.
			// ����Controller ���� ��ȯ��.
		
		
	}
	
	
}
/*
���⼭ ���! �����!
BoardDAOŬ������ �޼ҵ� �̸��� ���� ���޼ҵ���� �����ϴ�SQL���� ���� ���� �˴ϴ�.
���� ��� selectAllArticles()�޼ҵ�� ��ü �� ������ ��ȸ�ϴ� SQL���� �����ϹǷ�
�޼ҵ��̸��� selectAll�� ��� ���ϴ�.
*/
