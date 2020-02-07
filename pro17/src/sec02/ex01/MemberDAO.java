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

//자바 빈 클래스의 종류 -> DAO ,VO또는 DTO

//DAO 클래스의 역활 : DB에 연결하여 DB작업 하는 클래스


public class MemberDAO {

	//DB작업 관련 필요한 객체를 저장할 변수 선언. 그뭐 ojdbc6.jar 이런거도있음
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource ds;
	
	//커넥션플(DataSource)객체를 얻는 생성자
	public MemberDAO(){
		try{
			//InitialContext 객체가 하는 역활은 톰캣 실행시  context.xml에 의해ㅓ 생성된
			//Context 객체에 접근을 하는 역할을함.
			
			Context ctx = new InitialContext();
			
			
			//JDNI 방법으로 접근하기 위해 기본경로(java:/comp/env)를 지정합니다.
			//환결설정에 관련된 컨텍스트 객체에 접근하기 위한 기본 주소입니다.
			
			Context envConText = (Context)ctx.lookup("java:/comp/env");
			
			/*커넥션플 자원 얻기*/
			//그런 후 다시톰켓은 context.xml에 그 설정한<Resource name = "jdbc/oracle"../>
			//태그의 name 속성값인 "jdbc/oracle를 이용해 톰켓에 미리 DB에 연결해놓은
			//Datasource객체(커넥션풀 역활을 하는 객체)를 받아옵니다.
			ds=(DataSource)envConText.lookup("jdbc/oracle");
			
			
		}catch(Exception err){
			err.printStackTrace();
		}
		
	}//생성자 끝
	//DB에 새 회원정보를 INSERT 추가 시킬 메소드
	//매개변수로 MemberBean 객체를 전달받은 이유는?
	// Insert문장의 ? ? ? ? 에 대응되는 INSERT할 값을 만들기 위함;
	public void addMember(MemberVO memberBean){
		try{
			// 커넥션풀(Datasource) 객체안의 미리 DB연결한 정보를 지니고 있는 Connection 객체를 빌려옴
			con = ds.getConnection();
			
			//Insert문장을 만들기 위해 매개변수로 전달받은 MemberBean객체의 각변수값 리턴 받기
			
			String id = memberBean.getId();
			String pwd = memberBean.getPwd();
			String name = memberBean.getName();
			String email = memberBean.getEmail();
			
			//insert문장만들기
			String query = "insert into t_member(id,pwd,name,email)";
				   query += "values(?,?,?,?)";
				 
			// OraclePreparedStatementWrapper 실행객체 <-- INSERT문장을 DB에 전송하여 실행	   
			// ? 기호에 대응되는 설정값을 제외한 나머지 INSERT문장을 임시로
			// OraclePreparedStatementWrapper실행 객체에 담아
			// OraclePreparedStatementWrapper실행객체 자체를 반환받아 얻기
			pstmt = con.prepareStatement(query);
			
			// ?기호에 대응되는 설정값을 우리가 입력한 새 회원정보의 값으로 설정.
			//OraclePreparedStatementWrapper 실행객체에 ? 4개의 값을 설정.
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			
			//OraclePreparedStatementWrapper 실행객체를 이용하여
			//DB 테이블에 INSERT문장을 실행
			pstmt.executeUpdate();
			//사용자원 해제
			pstmt.close();
			con.close();
			
		}catch(Exception err){
			//이클립스의 console탭에 예외 메세지 출력
			err.printStackTrace();
		}
	}//addMember메소드 끝
	
	//DB에 저장되어 있는 모든 회원정보를(조회)검색하는 메소드
	public ArrayList listMembers(){ //member.jsp에서 호출하는 메소드
		
		//DB에 저장되어있는 모든 회원정보를 레코드단위(한명의 회원정보단위)로 검색해서
		//가져온 후 MemberBean객체에 각각 저장후 MemberBean객체물을 각각 추가하여 저장시킬 ArrayList배열 객체 생성
		
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
				
		try {
			//DB연결
			//커넥션풀(Datasource)객체안의 미리 DB연결한 정보를 지니고있는 Connection객체를 빌려옴
			
			con = ds.getConnection();
			//SQL 문 만들기 : 회원정보를 최근 가입일 순으로 내림차순 정렬하여 검색할 SELECT문 만들기
			String query = "select * from t_member order by joinDate desc";
			
			//?기호에  대응 되는 값을 제외한  SELECT문장을 임시로 OraclePreparedStatementWrapper실행객체에 담아
			//OraclePreparedStatementWrapper실행객체 에 반환받기 <-- SELECT 문장을 DB에 전송하여 실행할 역활
			pstmt = con.prepareStatement(query);
			
			//위의 query 변수에 저장된 select 문장을 DB에 전송하여 검색한 그 결과를
			//MemberDAO.java페이지로 전달받기 위해서
			//검색결과 데이터들을 Table형식의 구조로 저장할 임시 저장소 역활을 하는 객체가 필요하다.
			// 그 객체가 OracleResultSetImp 객체인 것이다.
			// OracleResultSetImp 객체에 검색한 결과 데이터를 Table형식의 구조로 똑같이 저장하여
			// OracleResultSetImp 객체자체를 리턴받는다.
			
			ResultSet rs = pstmt.executeQuery();
			
			//OracleResultSetImp 객체의 구조는 Table형식의 구조로서
			// 처음에는 커서(화살표:데이터를 가리키는 줄자)가 컬럼명이 있는 줄을 가리키고 있따.
			// rs.next()메소드를 호출 하면 커서 위치가 한줄 아래로 내려오면서
			// 그다음 줄에 레코드가 존재하는지 듣기됩니다.
			// next() 메소드는 그다음 줄에 검색한 레코드가 존재하면 true값을 반환하고 
			// 존재하지 않으면 false값을 반환합니당.
			
			
			while(rs.next()){
				
				//오라클 DB의 t_member테이블에서 검색한 레코드의 각 컬럼값을
				//OracleResultSetImp객체에서 꺼내와변수에 저장
				String id= rs.getString("id");
				String pwd= rs.getString("pwd");
				String name= rs.getString("name");
				String email= rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				//검색한 회원정보를 MemberBean객체의 각변수에 저장하기
				MemberVO vo = new MemberVO(id, pwd, name, email, joinDate);
				/*vo.setId(id);
				vo.setId(pwd);
				vo.setId(name);
				vo.setId(email);
				vo.setJoinDate(joinDate);*/
				
				
				//ArrayList배열에 MemberBean객체를 추가하여 저장.
				list.add(vo);
				
				
			}//while반복문 끗
			
			
			
			
		} catch (Exception err) {
		
			err.printStackTrace();
		}
		
		return list; // 검색한 회원정보들(MemberBean 객체들)을 저장하고있는 ArrayList반환
		
		
	}// listMembers 메소드 끗
	
	
	
	
	
	
}//MemberDAO 클래스 끝
