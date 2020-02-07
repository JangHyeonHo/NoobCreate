package sec02.ex02;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//��Ʈ�ѷ� ������ �ϴ� MemberController ���� Ŭ����
//�� ��Ʈ�ѷ������� request�� getPathInfo() �޼ҵ带 �̿��� �δܰ�� �̷���� ��û�ּҸ� �����ɴϴ�.
//action ���� ���� ���� if���� �б��ؤ� ��û�� �۾��� �����մϴ�.

// listMembers.jsp ���������� ȸ������ ��ũ Ŭ����
// /member/memberForm.do �ּҷ� ��û�� ����(ȸ������ �Է� �������� �̵� ������)

//memberForm.jsp ���������� DB�� �߰��� ȸ������ �Է� �� �����ϱ� ��ư Ŭ����
// /member/addMember.do �ּҷ� ��û�� ����(DB�� ���ο� ȸ���߰� ����)

//Ŭ���̾�Ʈ�� ���������� �̿��� ��Ʈ�ѷ��� ��û�ϸ� request��ü�� getPathInfo()�޼ҵ带 �̿���
// ���� ��û �ּ��� /member/modMemberForm.do�� /member/modMember.do�� ������ �� 
// if ������ �б��Ͽ� �۾��� �����ϵ��� MemberControllerŬ������ �ۼ�����.

// listMembers.jsp���������� ȸ�������� ���� ���� ��ũ�� Ŭ��������
// ���� ��û �ּ��� /member/delMember.do �� ������ �� 
// if������ �б��Ͽ� �۾��� �����ϵ��� MemberControllerŬ���� �ۼ� ����.



//@WebServlet("/member/*") // ������������ ��û�� �δܰ�� ��û�� �̷���� = member�� 1�ܰ� / *�� 2�ܰ�
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
		
		
		//MVC�߿� View �ּ�  ������ �뵵
		String nextPage = null;
		
		//�ѱ�ó��
		request.setCharacterEncoding("UTF-8");
		
		//������ ���������� ����
		response.setContentType("text/html;charset=utf-8");
		
		//Ŭ���̾�Ʈ�� 2�ܰ��� ��û �ּҰ� ���
		String action = request.getPathInfo();
		// /memberForm.do �� ��ȯ���� getPathInfo��
		// /listMember.do
		// /memberForm.do
		// /addMember.do
		// /modMemberForm.do
		// /modMember.do
		System.out.println("action : " + action);
		
		//action ������ ���� ���� if���� �б��ؼ� ��û�� �۾��� �����ϴµ�
		//���� action ������ ���� null �̰ų� /listMembers.do�ΰ�쿡 ȸ���˻� ����� ������.
		if(action == null||action.equals("/listMembers.do")){
		
			//MemberDAO�� listMembers() �޼ҵ带 ȣ���Ͽ� ȸ������ ��ȸ ��û�� ����
			//DB�κ��� �˻��� ȸ�������� ArrayList�� ��ȯ
			ArrayList membersList =memberDAO.listMembers();
			
			//�̶� DB�κ��� �˻��� ȸ��������(ArrayList�� ��� MemberVO��ü��)�� View�������� 
			//�����ϱ����� �ӽ� ���� ������ request���尴ü ������ ����(���ε�)��.
			//request.setAttribute("�Ӽ���(Ű)", "���䰪ArrayList->membersList");
			request.setAttribute("membersList", membersList);
			
			//�˻��� ȸ������(����޼���)�� ������ View������ �ּ� ����
			//= test02 ������ listMembers.jsp �� ������ �ϱ� ���� �ּ�����.
			nextPage = "/test03/listMembers.jsp";
		
		//
		}else if(action.equals("/memberForm.do")){//ȸ������ �Է��������� �̵�������
			
			nextPage = "/test03/memberForm.jsp"; //ȸ������ �Է������� (View)�ּ� ����
			
		}else if(action.equals("/addMember.do")){//�Է��� ���ο� ȸ�������� DB�� �߰�����
			
			//��û����(�Է��� ���ο� ȸ��������) request�������� ���
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			//MemberVO�� ����.
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			
			//��û�� ȸ�������� DB ���̺� insert�ϱ����� �޼ҵ� ȣ��
			memberDAO.addMember(memberVO);
			
			//���ο� ȸ�������� DB�� ���̺� insert�� �����ϸ� ���������� ������ �޼��� ����.
			request.setAttribute("msg", "addMember");
			//ȸ������ ������ �ٽ�ȸ������� �˻��ؼ� �����ִ� view�������� �̵��ϱ�����
			//�ٽ� MemberController.javat������ ���û�� �ּ� ����
			nextPage="/member/listMembers.do";
		

			//listMembers.jsp ���������� ������ũ�� Ŭ��������
			//��Ʈ�ѷ��� ȸ������ ����â�� ��û�� ID�� ȸ�������� ���� ��ȸ���� ����â���� ��������.	
				
		}else if(action.equals("/modMemberForm.do")){
			
			//�����ϱ� ���� ����Ȱ ȸ�� ID�� ���޹޾� �˻��ϱ� ����.
			String id = request.getParameter("id");
			
			//������ ȸ�� ID�� �ش��ϴ� ȸ������ �˻�
			MemberVO memberInfo = memberDAO.findMember(id);
			
			//������ ȸ�� �Ѹ��� ������ �˻��ؼ� �����ͼ� View ������(����â)���� �����ϱ�����
			//request���� ��ü�� ����
			request.setAttribute("memInfo", memberInfo);
			
			//ȸ������ ����â View�������� �������ϱ� ���� �ּ� ����
			nextPage ="/test03/modMemberForm.jsp/";
		
		//ȸ������ ����â(modMemberForm.jsp)���� ȸ������������ �Է� �� �����ϱ� ��ưŬ���� ��
		// /member/modMember.do�ּҷ� DB���̺� ����� ȸ�������� ���� ��û�� ������
		}else if(action.equals("/modMembers.do")){
			//��û�� ���(ȸ����������)
			String id= request.getParameter("id");
			String pwd= request.getParameter("pwd");
			String name= request.getParameter("name");
			String email= request.getParameter("email");
			//MemberVO�� �� ������ ����
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			//DBȸ�� ���̺��� ������ ���� ��û
			memberDAO.modMember(memberVO);//UPdate
			//����(UPDATE�� �����ϸ� listMembers.jsp�� �����۾��Ϸ� �޼����� �����ϱ�����
			//request�� �޼��� ����
			request.setAttribute("msg", "modified");
			//���� �� ��� ȸ���� �˻��Ͽ� �����ֱ� ����
			//MemberController.java �������� ��û�� �ּ� ����
			nextPage = "/member/listMembers.do";
			
		}else if(action.equals("/delMember.do")){//���� ��û�� ��������
			//������ ȸ���� ID�� �޾� �ɴϴ�.
			String id = request.getParameter("id");
			
			//������ ȸ�� ID�� �̿��Ͽ� DB�� ����� ȸ������ �۾� ���
			memberDAO.delMember(id);
			
			//������ �����ϸ� ȸ�����â(listMembers.jsp) View ��������
			//���� �Ϸ� �޼����� ����ϱ� ���� �޼����� request���� ��ü ������ ����.
			request.setAttribute("msg", "deleted");
			//request.setAttribute("msg","modified");
			
			//���� ���� �� �ٽ� ��� ȸ���� �˻� �ϱ� ���� ��û 
			nextPage = "/member/listMembers.do";
			
		}else{//�׿� action������ �ٸ� ��û URL�� ����Ǿ������� ȸ������� �����.
			
			ArrayList memberList = memberDAO.listMembers();
			request.setAttribute("memberList", memberList);
			nextPage="/test03/listMembers.jsp";
		}
		
		
		//nextPage ������ �ּҸ� ���� ������(���û) ��
		RequestDispatcher dispatche = request.getRequestDispatcher(nextPage);
		dispatche.forward(request, response);
		
		//<jsp:forward page="/test01/listMembers.jsp"> �̰Ŷ� ������ ����Դϴ�. �ٴ� ���� jsp�� ���� java���� ����
		
	}
	
	
}
