package javachatting;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class log_in_server {
	  private InputStream is;
	  private OutputStream os;
	  private ObjectInputStream ois;
	  private DataOutputStream dos;
	  private int check;
	  private ServerSocket log_in_server_socket; //ȸ��Ȯ�� ��������
	  private Socket log_in_socket; // ȸ��Ȯ�� ����

	  
	  public log_in_server(){
		  try{log_in_server_socket=new ServerSocket(8082);
		  server_access();
		  }
		  catch(Exception e){System.out.println("�α��� ���� ���� ��" +e+"	�߻�!"); }
	  }
	  
	  public void server_access(){
		  
		  
		  try {
			   if(log_in_server_socket!=null) // socket �� ���������� ��������
			   {
				   Connection();
			   }
			  } 
		  catch(Exception e){System.out.println("ȸ�� ���� Ȯ�� ���� ���� �� "+e+"�߻�");
				}
	  }
	  
	 public void Connection() { //Connection method starts//
			 
		  Thread log_in_th = new Thread(new Runnable() { // ȸ������ ������ ���� ������
	      @Override
	      public void run() {
	    	  while (true) {
	    		  try {
	    			  log_in_socket = log_in_server_socket.accept(); // accept�� �Ͼ�� �������� ���� �����
	    			  log_in_Info login_info=new log_in_Info(log_in_socket);
	    			  login_info.start();
	    		  	} catch (IOException e) {
			    	 	System.out.println("�α��� ���� ���� �� "+e+" �߻�!");
			     }
			    }
			   }
			  });
		  log_in_th.start();
			 } //Connection method ends//
	 
	 class log_in_Info extends Thread{	//�α��� ������ �ְ� �޴� joiningInfo class starts//
		  private InputStream is;
		  private OutputStream os;
		  private ObjectInputStream ois;
		  private DataOutputStream dos;
	      private int check;
		  private Socket login_info_socket;
	      private String info_array[]=new String[2];
	      
		  public log_in_Info(Socket soc) // �����ڸ޼ҵ�
		  {
		   // �Ű������� �Ѿ�� �ڷ� ����
		   this.login_info_socket = soc;
		   login_info_stream();
		  }
		  
		  public void login_info_stream() { //ȸ�� Ȯ�� ��Ʈ���� �����ϴ� login_info_stream() method//
			   try {
			    is = login_info_socket.getInputStream();
			    ois = new ObjectInputStream(is);
			    os = login_info_socket.getOutputStream();
			    dos = new DataOutputStream(os);
			    
			   } catch (Exception e) {
				   System.out.println("ȸ�� Ȯ�� ���� Ŭ������ login_info_stream() method���� "+e+" �߻�!");
			   }
			  }	//ȸ�� Ȯ�� ��Ʈ���� �����ϴ� login_info_stream() method//
		  
		  public void run(){
			  while(true){
				  try{
					  user_info u_info=(user_info)ois.readObject();
					  info_array[0]=u_info.getId();
					  info_array[1]=u_info.getPasswd();
					  database_select db_select=new database_select(info_array[0],info_array[1]);
					  check= db_select.select_check();//�α����� ����� �Ǿ����� ���� ������//
					  dos.writeUTF(String.valueOf(check));				  
				  }
				  
				  catch(IOException e){
					  try{
					      dos.close();
					      ois.close();
					      login_info_socket.close();
					      System.out.println("������� ȸ������ ������ ������");
					      break;
					  }
					  
					  catch(Exception ee){
						  System.out.println("log_in_server class�� log_in_Info class, run() method���� "+ee+" �߻�!");
					  }					  
					  
				  }
				  
				  catch(ClassNotFoundException e){
					  					  
				  }
				  
			  }
			  
		  }
		 
	 }	//�α��� ������ �޴� class ends//
	
	 public int check_result(){
		 return check;
	 }
	  
}

