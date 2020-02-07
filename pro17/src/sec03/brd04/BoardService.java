package sec03.brd04;

import java.util.List;

//�λ���
//BoardDAO ��ü�� ������ �� selectAllArticles()�޼ҵ带 ȣ���� ��ü ���� �˻��ؼ� �����ɴϴ�.
public class BoardService {

	BoardDAO boardDAO;
	
	
	public BoardService(){
		boardDAO = new BoardDAO();
	}
	
	//���ο� ���� DB�� �߰���Ű������ �޼ҵ� ȣ��
	//���߰��� �����ϸ� �۹�ȣ�� ��ȯ�ؼ� ��Ʈ�ѷ��� ������.
	
	public int addArticle(ArticleVO articleVO){
		
		return boardDAO.insertNewArticle(articleVO);
		
	}
	
	
	
	public List<ArticleVO> listArticles(){//BoardController������� ȣ���ϴ� �޼ҵ�
		
		//������ BoardDAO�� selectAllArticles() �޼ҵ� ȣ���� ��ü���� �˻��ؼ�(list��) ��ȯ����
		List<ArticleVO> articleList = boardDAO.selectAllArticles();
		
		return articleList;//�˻��� ��ü �� ������(ArticleVO��)�� ��� �ִ� ArrayList ������ ��ȯ.
			// ����Controller ���� ��ȯ��.
		
		
	}

	//BoardController ���� ȣ���� �޼ҵ�� �󼼺� �۹�ȣ�� �Ű������� ���޹޾�
	
	
	public ArticleVO viewArticle(int articleNO) {
		
		//BoardDAO�� selectArticle()�޼ҵ�ȣ��� ���ڷ� �����Ͽ� �ϳ��� ������ �˻� ��û�� ��.
		//�˻��� ����� ArticleVO ��ü�� ��ȯ�޴´�.
		ArticleVO article = boardDAO.selectArticles(articleNO);
		
		return article;//�� �� BoardController�� �ٽ� ArticleVO�� �����Ѵ�.
	
	}
	
	
}//BoardService Ŭ���� ��
/*
���⼭ ���! �����!
BoardDAOŬ������ �޼ҵ� �̸��� ���� ���޼ҵ���� �����ϴ�SQL���� ���� ���� �˴ϴ�.
���� ��� selectAllArticles()�޼ҵ�� ��ü �� ������ ��ȸ�ϴ� SQL���� �����ϹǷ�
�޼ҵ��̸��� selectAll�� ��� ���ϴ�.
*/
