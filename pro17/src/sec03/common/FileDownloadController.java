package sec03.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//viewArticle.jsp에서 전송한 글 번호와 이미지 파일이름으로 파일 경로를 만든 후 
//해당 파일을 내려 받습니다.
@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet{

	private static String ARTICLE_IMAGE_REPO = "c:\\board\\article_image";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		//이미지 파일 이름과 글번호를 가져 옵니다.
		String imageFileName = (String)request.getParameter("imageFileName");
		String articleNO = request.getParameter("articleNO");
		
		OutputStream out = response.getOutputStream();
		
		//글번호에 대한 파일 경로를 설정함.
		String path = ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName;
		File imageFile = new File(path);
		
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachement;fileName=" + imageFileName);
		
		FileInputStream in = new FileInputStream(imageFile);
		
		byte[] buffer = new byte[1024*8];
		
		while (true) {
			int count = in.read(buffer);
			if(count == -1){
				break;
			}
			out.write(buffer, 0, count);
		}		
		in.close();
		out.close();
	}	
}







