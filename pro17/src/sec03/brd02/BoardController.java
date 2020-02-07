package sec03.brd02;

import java.io.File;
import java.io.IOException;
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

// 서블릿 요청 주소 : /board/listArticles.do 주소로 DB에 저장된 전체글목록 검색 요청함.

// 컨트롤러 담당하는 BoardController 서블릿 클래스입니다.
// 주요역할 : action변수의 값이 /articleForm.do이면 글쓰기 창으로 웹브라우저를 이용해 요청하고,
// action변수의 값이 /addArticle.do 이면 다음 과정으로 입력한 글쓴 내용을 DB에 추가하는 작업을 함.
// 아래에 우리 개발자가 직접만든 upload() 메소드를 호출해 글쓰기창에서 전송된  글관련 정보를
// Map에 key/value 쌍으로 저장합니다.
// 파일을 첨부한 경우 먼저 파일 이름을 Map에 저장한 후 첨부한 파일을 저장소에 업로드 합니다.
// upload()메소드를 호출한 후에는 반환한 Map에서 새글 정보를 가져옵니다.
// 그런다음 service클래스의 addArticle()메소드의 인자로 새글 정보를 전달하면 새글이 등록됨.


//@WebServlet("/board/*") // /board/articleForm.do 가 BoardController 객체로 들어감.
public class BoardController extends HttpServlet{
	
	//글쓰기 화면에서 글 내용을 작성할때 첨부한 파일의 업로드 경로 위치를 상수로 선언
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	
	
	BoardService boardService;
	ArticleVO articleVO;
	
	
	//서블릿 초기화시 BoardService객체를 생성 함.
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
		//뷰페이지 주소를 저장할 변수 선언
		String nextPage = "";
		
		//한글처리
		request.setCharacterEncoding("UTF-8");
		
		//클라이언트의 웹브라우저로 응답할 데이터유형 설정
		response.setContentType("text/html; charset=utf-8");
		
		//요청 주소(URL) 받아 오긔  / 2단계 요청 주소 받아 오는게 getPathInfo
		String action = request.getPathInfo(); // /articleForm.do
											   // /addArticle.do
		
		System.out.println("action : " + action);
		//listArticles.do
		
		try {
			List<ArticleVO> articlesList;
			if(action == null){//요청명이 null일떄
				
				//DB로 부터 모든 회원정보 검색 요청!(부사장 BoardService에게)
				articlesList = boardService.listArticles();
				//검색한 회원정보인 ArrayList를 request객체 메모리에 저장해서 유지
				request.setAttribute("articlesList", articlesList);
				//재요청할 뷰 이름을 nextPage변수에 저장.
				nextPage = "/board02/listArticles.jsp"; // V
				
			}else if(action.equals("/listArticles.do")){
				
				//DB로 부터 모든 회원정보 검색 요청!(부사장 BoardService에게)
				articlesList = boardService.listArticles();
				//검색한 회원정보인 ArrayList를 request객체 메모리에 저장해서 유지
				request.setAttribute("articlesList", articlesList);
				//재요청할 뷰 이름을 nextPage변수에 저장.
				nextPage = "/board02/listArticles.jsp"; // V
				
			
			//listArticles.jsp에서 글쓰기 링크를 클릭했을때
			//글을 쓸수있는 화면으로 이동 시켜줘 라는 요청이 들어왔을때
			}else if(action.equals("/articleForm.do")){
				nextPage = "/board02/articleForm.jsp";//글쓰기 화면 
			//articleForm.jsp에서 추가할 새글 내용을 입력 하고 글쓰기 버튼을 눌렀을때
			//글 추가 요청이 들어왔을때 상황/
			}else if(action.equals("/addArticle.do")){
				// upload()메소드를 호출해 글쓰기 화면에서 입력한 글 관련 정보를
				// HashMap에 key와 value를 쌍으로 저장합니다.
				// 그런 후 글 입력시 추가적으로 업로드 할 파일을 선택하여 글쓰기 요청을 했따면
				// 업로드 할 파일명, 입력한 글 제목, 입력한 글 내용을 key/value 형태의 값들로 저장되어 있는
				// HashMap을 리턴 받는다.
				// 그렇지 않은 경우에는 업로드 한 파일명을 제외한 입력한 글 제목, 입력한 글 내용을 key/value형태의 값들로 저장되어 있는
				// HashMap을 리턴받는다.
				
				Map<String, String> articleMap = upload(request, response);
				
				// HashMap에 저장된 작성한 글데이터(업로드할 파일명, 입력한 글 제목, 입력한 글 내용)를
				// HashMap에서 꺼내오기
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				//articleVO에 저장
				//DB에 새글정보를 추가하기위해 사용자가 입력한 글 정보 + 업로드할 파일명을
				//ArticleVO객체의 각 변수에 저장.
				articleVO = new ArticleVO();
				articleVO.setParentNO(0);//추가할 새글의 부모글번호를 0으로 저장
				articleVO.setId("hong");//추가할 새글의 ID를 hong로 저장
				articleVO.setTitle(title);//추가할 새글 제목 저장.
				articleVO.setContent(content);//입력하여 추가할 새글 내용 저장.
				articleVO.setImageFileName(imageFileName);//업로드 할 파일명을 저장
				
				//그런다음 BoardService클래스의 addArticle()메소드 호출시
				//인자로 새글 정보를 전달 하면 새글이 DB에 등록됨
				// 요약 : 글쓰기 창에서 입력된 정보를 HashMap에서 꺼내어서 articleVO에 설정한 후
				//BoardService 객체의 addArticle()메소드로 전달하여 Insert작업을 명령함.
				boardService.addArticle(articleVO);
				
				//DB에 새글을 추가 하고 BoardController서블릿에서
				// /board02/listArticle.jsp로 이동하여
				//현재 글 정보를 다시 DB에서 검색 하여 보여주기위해 다음과 같은 요청 주소를 저장
				nextPage = "/board/listArticles.do";
				
			}
			
			
			// 디스패치 방식으로 포워딩(재요청)
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			//재요청
			dispatch.forward(request, response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("여기서 틀림");
		}
		
	}//doHandle 끗

	//파일 업로드 처리를 위한 upload메소드
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		
		Map<String, String> articleMap = new HashMap<String, String>();
		
		String encoding = "UTF-8";
		
		// 글쓰기를 할때 첨부한 이미지를 저장할 폴더 경로에 대해 접근하기 위한 File객체 생성
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		
		// 업로드할 파일 데이터를 임시로 저장할 객체 메모리
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		// 파일 업로드시 사용할 임시 메모리 최대 크기 1메가 바이트로 지정
		factory.setSizeThreshold(1024*1024*1);
		
		// 임시메모리에 파일 업로드시 지정된 1메가 바이트 크기를 넘길 경우 실제 업로듣 될 파일 경로를 지정함.
		factory.setRepository(currentDirPath);
		
		// 참고
		// DiskFileItemFactory 클래스는 업로드 파일의 크기가 지정한 크기를 넘기 전까지는
		// 업로드한 파일 데이터를 임시 메모리인 DiskFileItemFactory 객체 메모리 (임시메모리)에 저장을 하고 
		// 지정한 크기를 넘길 경우 업로드될 경로에 파일 업로드 (저장)한다.
		
		// 파일 업로드 할 메모리를 생성자 쪽으로 전달받아 저장한 파일 업로드를 처리할 객체 생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			// 업로드 할 파일에 대한 요청 정보가 저장되어 있는request 객체를 
			// ServletFileupload 객체의 parseRequest메소드 호출시 인자로 전달 하면
			// request객체에 저장되어 있는 정보를 파싱해서(추출해서)
			// DiskFileItem객체에 저장한 후
			// DiskFileItem객체들을 ArrayList 에 추가합니다.
			// 그 후 ArrayList를 반환합니다.
			
			List items = upload.parseRequest(request);
			
			//ArrayList 크기 만큼 (DiskFileItem 객체의 갯수만큼)반복
			for(int i=0; i<items.size(); i++){
				//ArrayList가변길이 배열에서 DiskFileItems 객체의 정보(아이템 하나의 정보)를 얻는다.
				FileItem fileItem = (FileItem)items.get(i);
				
				//DiskFileItem 객체의 정보가 파일 아이템이 아닐 경우
				if(fileItem.isFormField()){
					System.out.println(fileItem.getFieldName() + " = " + fileItem.getString(encoding));
					//articleForm.jsp페이지에서 입력한 글 제목, 글 내용만 따로 HashMap에 key,value형식으로 저장함.
					//HashMap 에 저장된 데이터의 예 -> (title = 입력한 글 제목, content= 입력한 글 내용)
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}else{//DiskFileItem객체의 정보가 파일 아이템일 경우에는?
					System.out.println("파라미터명 : " + fileItem.getFieldName());
					System.out.println("업로드 할 파일명 : " + fileItem.getName());
					System.out.println("업로드 할 크기 : " +fileItem.getSize()+"bytes" );
					
					//articleForm.jsp 에서 입력한 글 제목, 글 내용, 요청한 업로드 파일 정보등
					// 모든 정보를 HashMap에 key,value형식으로 저장합니다.
					//(imageFileName=업로드할 파일 이름)
					articleMap.put(fileItem.getFieldName(), fileItem.getName());
					
					//전체 : 업로드 할 파일이 존재 할 경우 업로드 할 파일의 파일 이름을 이용해 저장소에 업로드 함.
					//업로드 할 파일 크기가 0보다 크다면(업로드 할 파일이 존재 한다면)
					if(fileItem.getSize() > 0){
						//업로드 할 파일 명을 얻어 뒤에서 부터 \\ 문자열이 들어가 있는지 그 인덱스 위치를 알려주는데
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if(idx == -1){
							idx = fileItem.getName().lastIndexOf("\\"); // - 1 값을 얻기 위함.
						}
						//업로드 할 파일명 얻기
						String fileName= fileItem.getName().substring(idx + 1);
						
						//업로드 할 파일 경로 + 파일명을 이용하여 업로드 할 파일에 대한 객체 생성
						File uploadFile =new File(currentDirPath+"\\"+fileName);
						// 실제 파일 업로드
						fileItem.write(uploadFile);
						
					}//end if
				}//end if
			}//end for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleMap;//해쉬맵 리턴
	}//upload 메소드 끗

	
	
}//BoardController끗
