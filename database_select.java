package javachatting;

import java.sql.*;
import java.util.Calendar;

public class database_select {

	String id, password, last_access;
	Connection con;
	Statement select_st, update_st;
	ResultSet select_result;
	int i=0;
	
	public database_select(String id, String password){
		
		this.id=id;
		this.password=password;
		access_database();
	}
	
	public void access_database(){		//access_database �޼ҵ� ����
		try{
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/server","root","1234");
			select_st=con.createStatement();
			checkId();
		}
		
		catch(Exception e){
			System.out.println("�α��� ���� �����ͺ��̽� ���� �� �߻��� "+e);			
		}
	}		//access_database �޼ҵ� ��
	
	public void checkId(){											//ȸ�� ���̵�� �н����带 �˻��ϴ� �޼ҵ� ����//
				String select_query="select passwd from members where id='"+id+"';";
				try{
				select_result=select_st.executeQuery(select_query);
				select_result.next();
					if(password.equals(select_result.getString(1))){	//ȸ�� ���̵�� �н����尡 ��ġ�� ��//
						access_update();
						i=1;
						System.out.println(id+"�� �α��� ���!");
						}

				}
				
				catch( java.sql.SQLException e){					//ȸ�� ���̵�� �н����尡 ��ġ���� ���� ��//
					System.out.println("���̵�� �н����� Ȯ�� �� �߻��� "+e);
					i=2;}

			}														//ȸ�� ���̵�� �н����带 �˻��ϴ� �޼ҵ� ��//
	
	public void access_update(){									//������ ���� ��¥�� �����ϴ� �޼ҵ� ����//
		
			Calendar cal=Calendar.getInstance();
			String year=String.valueOf(cal.get(Calendar.YEAR));
			String month=String.valueOf(cal.get(Calendar.MONTH)+1);
			String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			last_access=year+month+day;
			
			try{
				update_st=con.createStatement();
				String update_query="update members set last_access="+last_access+" where id='"+id+"';";
				update_st.executeUpdate(update_query);
			}
			
			catch(Exception e){
				System.out.println("���� ��¥ ���� �� �߻��� "+e);				
			}
	}																//������ ���� ��¥�� �����ϴ� �޼ҵ� ��//
	
	public int select_check(){
		return i;		
	}
	
}

