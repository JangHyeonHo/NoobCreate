package sec03.brd04;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

// ������ ��û �ּ� : /board/listArticles.do �ּҷ� DB�� ����� ��ü�۸�� �˻� ��û��.

// ��Ʈ�ѷ� ����ϴ� BoardController ������ Ŭ�����Դϴ�.
// �ֿ俪�� : action������ ���� /articleForm.do�̸� �۾��� â���� ���������� �̿��� ��û�ϰ�,
// action������ ���� /addArticle.do �̸� ���� �������� �Է��� �۾� ������ DB�� �߰��ϴ� �۾��� ��.
// �Ʒ��� �츮 �����ڰ� �������� upload() �޼ҵ带 ȣ���� �۾���â���� ���۵�  �۰��� ������
// Map�� key/value ������ �����մϴ�.
// ������ ÷���� ��� ���� ���� �̸��� Map�� ������ �� ÷���� ������ ����ҿ� ���ε� �մϴ�.
// upload()�޼ҵ带 ȣ���� �Ŀ��� ��ȯ�� Map���� ���� ������ �����ɴϴ�.
// �׷����� serviceŬ������ addArticle()�޼ҵ��� ���ڷ� ���� ������ �����ϸ� ������ ��ϵ�.


//@WebServlet("/board/*") // /board/articleForm.do �� BoardController ��ü�� ��.
						// /board/viewArticle.do?articleNO= �ۺ�ȣ
public class BoardController extends HttpServlet{
	
	//�۾��� ȭ�鿡�� �� ������ �ۼ��Ҷ� ÷���� ������ ���ε� ��� ��ġ�� ����� ����
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	
	
	BoardService boardService;
	ArticleVO articleVO;
	
	
	//������ �ʱ�ȭ�� BoardService��ü�� ���� ��.
	@Override
	public void init() throws ServletException {
		boardService = new BoardService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�������� �ּҸ� ������ ���� ����
		String nextPage = "";
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//Ŭ���̾�Ʈ�� ���������� ������ ���������� ����
		response.setContentType("text/html; charset=utf-8");
		
		//��û �ּ�(URL) �޾� ����  / 2�ܰ� ��û �ּ� �޾� ���°� getPathInfo
		String action = request.getPathInfo(); // /articleForm.do
											   // /addArticle.do
											   // /viewArticle.do
		System.out.println("action : " + action);
		//listArticles.do
		
		try {
			List<ArticleVO> articlesList;
			
			if(action == null){//��û���� null�ϋ�
				
				//DB�� ���� ��� ȸ������ �˻� ��û!(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				//�˻��� ȸ�������� ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				//���û�� �� �̸��� nextPage������ ����.
				nextPage = "/board03/listArticles.jsp"; // V
				
			}else if(action.equals("/listArticles.do")){
				
				//DB�� ���� ��� ȸ������ �˻� ��û!(�λ��� BoardService����)
				articlesList = boardService.listArticles();
				//�˻��� ȸ�������� ArrayList�� request��ü �޸𸮿� �����ؼ� ����
				request.setAttribute("articlesList", articlesList);
				//���û�� �� �̸��� nextPage������ ����.
				nextPage = "/board03/listArticles.jsp"; // V
				
			
			//listArticles.jsp���� �۾��� ��ũ�� Ŭ��������
			//���� �����ִ� ȭ������ �̵� ������ ��� ��û�� ��������
			}else if(action.equals("/articleForm.do")){
				
				nextPage = "/board03/articleForm.jsp";//�۾��� ȭ�� 
				
			//articleForm.jsp���� �߰��� ���� ������ �Է� �ϰ� �۾��� ��ư�� ��������
			//�� �߰� ��û�� �������� ��Ȳ/
			}else if(action.equals("/addArticle.do")){
				
				
				Map<String, String> articleMap = upload(request, response);
				
				
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				
				articleVO = new ArticleVO();
				articleVO.setParentNO(0);//�߰��� ������ �θ�۹�ȣ�� 0���� ����
				articleVO.setId("hong");//�߰��� ������ ID�� hong�� ����
				articleVO.setTitle(title);//�߰��� ���� ���� ����.
				articleVO.setContent(content);//�Է��Ͽ� �߰��� ���� ���� ����.
				articleVO.setImageFileName(imageFileName);//���ε� �� ���ϸ��� ����
				
				//t_board ���̺��� ���ο� ���� �߰��� �� �߰��� ���ۿ� ���� �� ��ȣ�� ��ȯ�޽��ϴ�.
				int articleNO = boardService.addArticle(articleVO);
				
				//������ ÷���� ��쿡�� �����մϴ�.
				if(imageFileName != null && imageFileName.length() !=0){
					
					//temp������ �ӽ÷� ���ε� �� ���� ��ü�� ������.
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName); // ���� ��ΰ� ������ �̹������� �ű� ��� ������.
					
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					//C:\\board\\article_image�� ��� ������ �� ��ȣ�� ������ ���� �մϴ�.
					destDir.mkdirs();
					
					//temp ������ ������ �۹�ȣ�� �̸����� �ϴ� ������ �̵� ��ŵ�ϴ�. ��°�
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>"
						+ " alert('������ �߰� �߽��ϴ�.');"
						+ " location.href='"+ request.getContextPath()+ "/board/listArticle.do';"
						+ "</script>");
				
				return;
				
				
			}else if(action.equals("/viewArticle.do")){//�� ������ Ŭ�������� ���� �� ������ �˻�����.
				
				//��û������.
				//���� �� �ۿ� ���� �� ��ȣ�� request���� ��������
				String articleNO = request.getParameter("articleNO");
				
				//���� ��Ȱ�� �ϴ� ��ü �޼ҵ� ȣ��� �Ͽ� �˻� ��û�� ��.
				//��ȯ�� - > �˻��� �� ������ ����� Artcion
				articleVO = boardService.viewArticle(Integer.parseInt(articleNO));
			
				//�˻��� �� ������ ����Ǿ� �ִ� ArticleVO ��ü�� ������ View�������� ���� �ϱ�����
				//request���尴ü ������ �����Ͽ� ������Ű��
				request.setAttribute("article",articleVO);
				
				
				//���û�� �������� �ּҸ� nextPage����������
				nextPage="/board03/viewArticle.jsp";
				
			}
			
			
			// ����ġ ������� ������(���û)
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			//���û
			dispatch.forward(request, response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���⼭ Ʋ��");
		}
		
	}//doHandle ��

	//���� ���ε� ó���� ���� upload�޼ҵ�
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		
		Map<String, String> articleMap = new HashMap<String, String>();
		
		String encoding = "utf-8";
		
		// �۾��⸦ �Ҷ� ÷���� �̹����� ������ ���� ��ο� ���� �����ϱ� ���� File��ü ����
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		
		// ���ε��� ���� �����͸� �ӽ÷� ������ ��ü �޸�
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		// ���� ���ε�� ����� �ӽ� �޸� �ִ� ũ�� 1�ް� ����Ʈ�� ����
		factory.setSizeThreshold(1024*1024*1);
		
		// �ӽø޸𸮿� ���� ���ε�� ������ 1�ް� ����Ʈ ũ�⸦ �ѱ� ��� ���� ���ε� �� ���� ��θ� ������.
		factory.setRepository(currentDirPath);
		
		// ����
		// DiskFileItemFactory Ŭ������ ���ε� ������ ũ�Ⱑ ������ ũ�⸦ �ѱ� ��������
		// ���ε��� ���� �����͸� �ӽ� �޸��� DiskFileItemFactory ��ü �޸� (�ӽø޸�)�� ������ �ϰ� 
		// ������ ũ�⸦ �ѱ� ��� ���ε�� ��ο� ���� ���ε� (����)�Ѵ�.
		
		// ���� ���ε� �� �޸𸮸� ������ ������ ���޹޾� ������ ���� ���ε带 ó���� ��ü ����
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			// ���ε� �� ���Ͽ� ���� ��û ������ ����Ǿ� �ִ�request ��ü�� 
			// ServletFileupload ��ü�� parseRequest�޼ҵ� ȣ��� ���ڷ� ���� �ϸ�
			// request��ü�� ����Ǿ� �ִ� ������ �Ľ��ؼ�(�����ؼ�)
			// DiskFileItem��ü�� ������ ��
			// DiskFileItem��ü���� ArrayList �� �߰��մϴ�.
			// �� �� ArrayList�� ��ȯ�մϴ�.
			
			List items = upload.parseRequest(request);
			
			//ArrayList ũ�� ��ŭ (DiskFileItem ��ü�� ������ŭ)�ݺ�
			for(int i=0; i<items.size(); i++){
				//ArrayList�������� �迭���� DiskFileItems ��ü�� ����(������ �ϳ��� ����)�� ��´�.
				FileItem fileItem = (FileItem)items.get(i);
				
				//DiskFileItem ��ü�� ������ ���� �������� �ƴ� ���
				if(fileItem.isFormField()){
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					//articleForm.jsp���������� �Է��� �� ����, �� ���븸 ���� HashMap�� key,value�������� ������.
					//HashMap �� ����� �������� �� -> (title = �Է��� �� ����, content= �Է��� �� ����)
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}else{//DiskFileItem��ü�� ������ ���� �������� ��쿡��?
					System.out.println("�Ķ���͸� : " + fileItem.getFieldName());
					System.out.println("���ε� �� ���ϸ� : " + fileItem.getName());
					System.out.println("���ε� �� ũ�� : " +fileItem.getSize()+"bytes" );
					
					//articleForm.jsp ���� �Է��� �� ����, �� ����, ��û�� ���ε� ���� ������
					// ��� ������ HashMap�� key,value�������� �����մϴ�.
					//(imageFileName=���ε��� ���� �̸�)
					articleMap.put(fileItem.getFieldName(), fileItem.getName());
					
					//��ü : ���ε� �� ������ ���� �� ��� ���ε� �� ������ ���� �̸��� �̿��� ����ҿ� ���ε� ��.
					//���ε� �� ���� ũ�Ⱑ 0���� ũ�ٸ�(���ε� �� ������ ���� �Ѵٸ�)
					if(fileItem.getSize() > 0){
						//���ε� �� ���� ���� ��� �ڿ��� ���� \\ ���ڿ��� �� �ִ��� �� �ε��� ��ġ�� �˷��ִµ�
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if(idx == -1){
							idx = fileItem.getName().lastIndexOf("/"); // - 1 ���� ��� ����.
						}
						//���ε� �� ���ϸ� ���
						String fileName= fileItem.getName().substring(idx + 1);
						
						//���ε� �� ���� ��� + ���ϸ��� �̿��Ͽ� ���ε� �� ���Ͽ� ���� ��ü ���� 
						File uploadFile =new File(currentDirPath+"\\temp\\"+fileName);
						// ���� ���� ���ε�
						fileItem.write(uploadFile);
						
					}//end if
				}//end if
			}//end for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleMap;//�ؽ��� ����
	}//upload �޼ҵ� ��

	
	
}//BoardController��