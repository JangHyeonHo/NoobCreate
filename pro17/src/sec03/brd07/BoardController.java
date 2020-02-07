package sec03.brd07;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

//답글도 새글이므로 답글기능도 새글쓰기 기능과 비슷합니다.
//다른점은 답글창 요청(/replyForm.do)시  미리 부모글 번호를 parentNO속성으로
//session세션영역에 저장해놓고,
//답글을 작성한 후 답글 등록 요청(/addReply.do)하면 session영역에서
//parentNO를 가져와 t_board테이블에 추가한다는 점 입니다.

//@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	BoardService boardService;
	ArticleVO articleVO;
	HttpSession session; //답글에 대한 부모 글번호를 저장하기 위해 세션을 사용함.
	
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
		articleVO = new ArticleVO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
									//board/removeArticle.do
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo(); // /reply.do
		System.out.println("action:" + action);
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
			if (action == null) {
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
				nextPage = "/board06/listArticles.jsp";
			} else if (action.equals("/listArticles.do")) {
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
				nextPage = "/board06/listArticles.jsp";
			} else if (action.equals("/articleForm.do")) {
				nextPage = "/board06/articleForm.jsp";
			} else if (action.equals("/addArticle.do")) {
				int articleNO = 0;
				Map<String, String> articleMap = upload(request, response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");

				articleVO.setParentNO(0);
				articleVO.setId("hong");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNO = boardService.addArticle(articleVO);
				if (imageFileName != null && imageFileName.length() != 0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					srcFile.delete();
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('새글을 추가했습니다.');" + " location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");

				return;
			} else if (action.equals("/viewArticle.do")) {
				String articleNO = request.getParameter("articleNO");
				articleVO = boardService.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article", articleVO);
				nextPage = "/board06/viewArticle.jsp";
			} else if (action.equals("/modArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				int articleNO = Integer.parseInt(articleMap.get("articleNO"));
				articleVO.setArticleNO(articleNO);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				articleVO.setParentNO(0);
				articleVO.setId("hong");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				boardService.modArticle(articleVO);
				if (imageFileName != null && imageFileName.length() != 0) {
					String originalFileName = articleMap.get("originalFileName");
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					;
					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
					oldFile.delete();
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('글을 수정했습니다.');" + " location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
				return;
			
			//삭제 요청 들어 왔을떄..
			}else if(action.equals("/removeArticle.do")){
				//삭제할 글번호를 request에서 꺼내오기 
				int articleNO = Integer.parseInt(request.getParameter("articleNO"));
				
				//삭제할 글번호(articleNO)에 대한 글을 삭제한후 삭제된 부모글과 자식글의 
				//글번호들을(articleNO들을) list에 담아서 리턴 받는다.
				//리턴 받는 이유? DB에 저장된 글을 삭제한 후  그글 번호로 이루어진 이미지 저장폴더까지
				//모두 삭제 하기 위함.
				List<Integer> articleNOList = boardService.removeArticle(articleNO);
			
				for(int _articleNO : articleNOList){
					
					File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
					
					if(imgDir.exists()){
						
						FileUtils.deleteDirectory(imgDir); // DB에 삭제한 글들의 이미지 저장 폴더를 삭제 함.
						
					}
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + " alert('글을 삭제 했습니다.');" + " location.href='"
						+ request.getContextPath() + "/board/listArticles.do';"
						+"</script>"
						);
				return;
				
			}else if(action.equals("/replyForm.do")){//답글을 작성할 수 있는 창 요청
				// 답글을 작성할 수 있는 창 요청시
				// 미리 부모글 번호를 세션영역에 저장.
				int parentNO = Integer.parseInt(request.getParameter("parentNO"));
				//세션 영역 하나 생성
				session = request.getSession();
				//세션영역에 부모글 번호 저장
				session.setAttribute("parentNO", parentNO);
				//재요청할 뷰 정보 저장
				nextPage = "/board06/replyForm.jsp";
				
			//replyForm.jsp에서 답글 내용을 입력하고 답글 반영하기 버튼을 클릭시 요청받음	
			}else if(action.equals("/addReply.do")){
				//기존의 session 영역에 저장된 parentNO를 가져옵니다.
				session = request.getSession(true);
				int parentNO= (Integer)session.getAttribute("parentNO");
				session.removeAttribute("parentNO");//session영역에 저장된 부모글 번호 제거.
				
				Map<String, String> articleMap = upload(request, response);
				String title= articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName= articleMap.get("imageFileName");
				
				//답글의 부모글 번호를 저장
				articleVO.setParentNO(parentNO);
				//답글 작성자 ID를 lee로 저장
				articleVO.setId("gong");
				//답글 내용중 제목을 저장
				articleVO.setTitle(title);
				//답글 내용 저장
				articleVO.setContent(content);
				//답글 작성이 첨부한 이미자피일명 저장
				articleVO.setImageFileName(imageFileName);
				
				//작성한 답글 전체 데이터(ArticleVO객체의 데이터)를 DB에 Insert하기 위해
				//BoardSerivce의 메소드 호출시 전달.
				System.out.println("getWriter3");
				int articleNO = boardService.addReply(articleVO);
				System.out.println("gerWriter4");
				//답글에 첨부된 이미지를 temp폴더에 답글번호 폴더로 이동합니다.
				if (imageFileName != null && imageFileName.length() != 0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
				}
				System.out.println("getWriter1");
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + " alert('답글을 추가했습니다.');" + " location.href='"
						+ request.getContextPath() 
						+ "/board/viewArticle.do?articleNO="
						+ articleNO + "' ;" + "</script>");
						
				System.out.println("getWriter2");
				return;
				
			}

			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					System.out.println("파라미터명:" + fileItem.getFieldName());
					//System.out.println("파일명:" + fileItem.getName());
					System.out.println("파일크기:" + fileItem.getSize() + "bytes");
					//articleMap.put(fileItem.getFieldName(), fileItem.getName());
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}

						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("파일명:" + fileName);
						//익스플로러에서 업로드 파일의 경로 제거 후 map에 파일명 저장
						articleMap.put(fileItem.getFieldName(), fileName);  
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);

					} // end if
				} // end if
			} // end for
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
	}

}
