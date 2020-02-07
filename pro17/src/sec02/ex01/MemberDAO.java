package sec02.ex01;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;

//�ڹ� �� Ŭ������ ���� -> DAO ,VO�Ǵ� DTO

//DAO Ŭ������ ��Ȱ : DB�� �����Ͽ� DB�۾� �ϴ� Ŭ����


public class MemberDAO {

	//DB�۾� ���� �ʿ��� ��ü�� ������ ���� ����. �׹� ojdbc6.jar �̷��ŵ�����
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource ds;
	
	//Ŀ�ؼ���(DataSource)��ü�� ��� ������
	public MemberDAO(){
		try{
			//InitialContext ��ü�� �ϴ� ��Ȱ�� ��Ĺ �����  context.xml�� ���ؤ� ������
			//Context ��ü�� ������ �ϴ� ��������.
			
			Context ctx = new InitialContext();
			
			
			//JDNI ������� �����ϱ� ���� �⺻���(java:/comp/env)�� �����մϴ�.
			//ȯ�ἳ���� ���õ� ���ؽ�Ʈ ��ü�� �����ϱ� ���� �⺻ �ּ��Դϴ�.
			
			Context envConText = (Context)ctx.lookup("java:/comp/env");
			
			/*Ŀ�ؼ��� �ڿ� ���*/
			//�׷� �� �ٽ������� context.xml�� �� ������<Resource name = "jdbc/oracle"../>
			//�±��� name �Ӽ����� "jdbc/oracle�� �̿��� ���Ͽ� �̸� DB�� �����س���
			//Datasource��ü(Ŀ�ؼ�Ǯ ��Ȱ�� �ϴ� ��ü)�� �޾ƿɴϴ�.
			ds=(DataSource)envConText.lookup("jdbc/oracle");
			
			
		}catch(Exception err){
			err.printStackTrace();
		}
		
	}//������ ��
	//DB�� �� ȸ�������� INSERT �߰� ��ų �޼ҵ�
	//�Ű������� MemberBean ��ü�� ���޹��� ������?
	// Insert������ ? ? ? ? �� �����Ǵ� INSERT�� ���� ����� ����;
	public void addMember(MemberVO memberBean){
		try{
			// Ŀ�ؼ�Ǯ(Datasource) ��ü���� �̸� DB������ ������ ���ϰ� �ִ� Connection ��ü�� ������
			con = ds.getConnection();
			
			//Insert������ ����� ���� �Ű������� ���޹��� MemberBean��ü�� �������� ���� �ޱ�
			
			String id = memberBean.getId();
			String pwd = memberBean.getPwd();
			String name = memberBean.getName();
			String email = memberBean.getEmail();
			
			//insert���常���
			String query = "insert into t_member(id,pwd,name,email)";
				   query += "values(?,?,?,?)";
				 
			// OraclePreparedStatementWrapper ���ఴü <-- INSERT������ DB�� �����Ͽ� ����	   
			// ? ��ȣ�� �����Ǵ� �������� ������ ������ INSERT������ �ӽ÷�
			// OraclePreparedStatementWrapper���� ��ü�� ���
			// OraclePreparedStatementWrapper���ఴü ��ü�� ��ȯ�޾� ���
			pstmt = con.prepareStatement(query);
			
			// ?��ȣ�� �����Ǵ� �������� �츮�� �Է��� �� ȸ�������� ������ ����.
			//OraclePreparedStatementWrapper ���ఴü�� ? 4���� ���� ����.
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			
			//OraclePreparedStatementWrapper ���ఴü�� �̿��Ͽ�
			//DB ���̺� INSERT������ ����
			pstmt.executeUpdate();
			//����ڿ� ����
			pstmt.close();
			con.close();
			
		}catch(Exception err){
			//��Ŭ������ console�ǿ� ���� �޼��� ���
			err.printStackTrace();
		}
	}//addMember�޼ҵ� ��
	
	//DB�� ����Ǿ� �ִ� ��� ȸ��������(��ȸ)�˻��ϴ� �޼ҵ�
	public ArrayList listMembers(){ //member.jsp���� ȣ���ϴ� �޼ҵ�
		
		//DB�� ����Ǿ��ִ� ��� ȸ�������� ���ڵ����(�Ѹ��� ȸ����������)�� �˻��ؼ�
		//������ �� MemberBean��ü�� ���� ������ MemberBean��ü���� ���� �߰��Ͽ� �����ų ArrayList�迭 ��ü ����
		
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
				
		try {
			//DB����
			//Ŀ�ؼ�Ǯ(Datasource)��ü���� �̸� DB������ ������ ���ϰ��ִ� Connection��ü�� ������
			
			con = ds.getConnection();
			//SQL �� ����� : ȸ�������� �ֱ� ������ ������ �������� �����Ͽ� �˻��� SELECT�� �����
			String query = "select * from t_member order by joinDate desc";
			
			//?��ȣ��  ���� �Ǵ� ���� ������  SELECT������ �ӽ÷� OraclePreparedStatementWrapper���ఴü�� ���
			//OraclePreparedStatementWrapper���ఴü �� ��ȯ�ޱ� <-- SELECT ������ DB�� �����Ͽ� ������ ��Ȱ
			pstmt = con.prepareStatement(query);
			
			//���� query ������ ����� select ������ DB�� �����Ͽ� �˻��� �� �����
			//MemberDAO.java�������� ���޹ޱ� ���ؼ�
			//�˻���� �����͵��� Table������ ������ ������ �ӽ� ����� ��Ȱ�� �ϴ� ��ü�� �ʿ��ϴ�.
			// �� ��ü�� OracleResultSetImp ��ü�� ���̴�.
			// OracleResultSetImp ��ü�� �˻��� ��� �����͸� Table������ ������ �Ȱ��� �����Ͽ�
			// OracleResultSetImp ��ü��ü�� ���Ϲ޴´�.
			
			ResultSet rs = pstmt.executeQuery();
			
			//OracleResultSetImp ��ü�� ������ Table������ �����μ�
			// ó������ Ŀ��(ȭ��ǥ:�����͸� ����Ű�� ����)�� �÷����� �ִ� ���� ����Ű�� �ֵ�.
			// rs.next()�޼ҵ带 ȣ�� �ϸ� Ŀ�� ��ġ�� ���� �Ʒ��� �������鼭
			// �״��� �ٿ� ���ڵ尡 �����ϴ��� ���˴ϴ�.
			// next() �޼ҵ�� �״��� �ٿ� �˻��� ���ڵ尡 �����ϸ� true���� ��ȯ�ϰ� 
			// �������� ������ false���� ��ȯ�մϴ�.
			
			
			while(rs.next()){
				
				//����Ŭ DB�� t_member���̺��� �˻��� ���ڵ��� �� �÷�����
				//OracleResultSetImp��ü���� �����ͺ����� ����
				String id= rs.getString("id");
				String pwd= rs.getString("pwd");
				String name= rs.getString("name");
				String email= rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				//�˻��� ȸ�������� MemberBean��ü�� �������� �����ϱ�
				MemberVO vo = new MemberVO(id, pwd, name, email, joinDate);
				/*vo.setId(id);
				vo.setId(pwd);
				vo.setId(name);
				vo.setId(email);
				vo.setJoinDate(joinDate);*/
				
				
				//ArrayList�迭�� MemberBean��ü�� �߰��Ͽ� ����.
				list.add(vo);
				
				
			}//while�ݺ��� ��
			
			
			
			
		} catch (Exception err) {
		
			err.printStackTrace();
		}
		
		return list; // �˻��� ȸ��������(MemberBean ��ü��)�� �����ϰ��ִ� ArrayList��ȯ
		
		
	}// listMembers �޼ҵ� ��
	
	
	
	
	
	
}//MemberDAO Ŭ���� ��
