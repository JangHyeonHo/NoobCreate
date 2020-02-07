package sec03.brd01;

import java.util.List;

//�λ���
//BoardDAO ��ü�� ������ �� selectAllArticles()�޼ҵ带 ȣ���� ��ü ���� �˻��ؼ� �����ɴϴ�.
public class BoardService {

	BoardDAO boardDAO;
	
	public BoardService(){
		boardDAO = new BoardDAO();
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
