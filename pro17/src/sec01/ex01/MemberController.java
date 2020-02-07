package sec01.ex01;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Ŭ���̾�Ʈ�� �� ������ �ּ� â�� �ּҸ� �Է� �Ͽ� �������� ��û�մϴ�.
// ��û���� : DB�� �����ϴ� ��� ȸ�������� �˻�����
// ��û�ּ� : http://localhost:8090/pro17/mem.do

//@WebServlet("/mem.do")
public class MemberController extends HttpServlet{
	
	MemberDAO memberDAO;
	
	//init() �޼ҵ忡�� MemberDAO��ü�� �����ؼ� �ʱ�ȭ
	
	@Override
	public void init() throws ServletException{
		memberDAO = new MemberDAO();
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
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//MemberDAO�� listMembers() �޼ҵ带 ȣ���Ͽ� ȸ������ ��ȸ ��û�� ����
		//DB�κ��� �˻��� ȸ�������� ArrayList�� ��ȯ
		ArrayList membersList =memberDAO.listMembers();
		
		//�̶� DB�κ��� �˻��� ȸ��������(ArrayList�� ��� MemberVO��ü��)�� View�������� 
		//�����ϱ����� �ӽ� ���� ������ request���尴ü ������ ����(���ε�)��.
		//request.setAttribute("�Ӽ���(Ű)", "���䰪ArrayList->membersList");
		request.setAttribute("memberList", membersList);
		
		//�׷� ���� RequestDispatcher Ŭ������ �̿��� ȸ�� ���â(listMembers.jsp View������)��
		// ������(���û) ��
		RequestDispatcher dispatche = request.getRequestDispatcher("/test01/listMembers.jsp");
		dispatche.forward(request, response);
		
		//<jsp:forward page="/test01/listMembers.jsp"> �̰Ŷ� ������ ����Դϴ�. �ٴ� ���� jsp�� ���� java���� ����
		
	}
	
	
}
