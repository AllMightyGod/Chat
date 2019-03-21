package javachatting;

import java.net.*;
import java.io.*;
import java.util.Vector;


class joining_server {

	private ServerSocket joining_server_socket; //ȸ������ ��������
	private Socket joining_socket; // ȸ������ ����

	
	public joining_server(){  //ȸ������ ���� starts, joining_server() constructor starts//
		  try {
			   joining_server_socket = new ServerSocket(8090); // ������ ��Ʈ ���ºκ�
			   if(joining_server_socket!=null) // socket �� ���������� ��������
			   {
				   Connection();
			   }
			  } catch (IOException e) {
				  System.out.println("ȸ������ ������ �̹� ������Դϴ�...");
				  System.exit(0);
			  }
			 } //joining_server() constructor ends//
	
	 public void Connection() { //Connection method starts//
		 
		  Thread joining_th = new Thread(new Runnable() { // ȸ������ ������ ���� ������
			  
	      @Override
		   public void run() {
		    while (true) { // ȸ������ ������ ����ؼ� �ޱ� ���� while��
		     try {
		      joining_socket = joining_server_socket.accept(); // accept�� �Ͼ�� �������� ���� �����
		      joiningInfo joining_info=new joiningInfo(joining_socket);
		      joining_info.start();
		     } catch (IOException e) {
		    	 	System.out.println("ȸ������ ���� �� "+e+" �߻�!");
		     }
		    }
		   }
		  });
		  joining_th.start();
		  		  
		 } //Connection method ends//
	 
	 class joiningInfo extends Thread{	//ȸ������ ������ �ְ� �޴� joiningInfo class starts//
		  private InputStream is;
		  private OutputStream os;
		  private ObjectInputStream ois;
		  private DataOutputStream dos;
	      private int check;
		  private Socket user_info_socket;
	      private String info_array[]=new String[6];
	      
		  public joiningInfo(Socket soc) // �����ڸ޼ҵ�
		  {
		   // �Ű������� �Ѿ�� �ڷ� ����
		   this.user_info_socket = soc;
		   user_info_stream();
		  }
		  
		  public void user_info_stream() { //ȸ�� ���� ��Ʈ���� �����ϴ� user_info_stream() method//
			   try {
			    is = user_info_socket.getInputStream();
			    ois = new ObjectInputStream(is);
			    os = user_info_socket.getOutputStream();
			    dos = new DataOutputStream(os);
			    
			   } catch (Exception e) {
				   System.out.println("ȸ������ ���� Ŭ������ user_info() method���� "+e+" �߻�!");
			   }
			  }	//ȸ�� ���� ��Ʈ���� �����ϴ� user_info_stream() method//
		  
		  public void run(){
			  while(true){
				  try{
					  user_info u_info=(user_info)ois.readObject();
					  info_array[0]=u_info.getName();
					  info_array[1]=u_info.getId();
					  info_array[2]=u_info.getPasswd();
					  info_array[3]=u_info.getEmail();
					  info_array[4]=u_info.getBirth();
					  info_array[5]=u_info.getSex();
					  database_insert db_insert=new database_insert(info_array[0],info_array[1],info_array[2],info_array[3],info_array[4],info_array[5]);
					  check= db_insert.insert_check();//ȸ�� ������ ����� �Ǿ����� ���� ������//
					  dos.writeUTF(String.valueOf(check));				  
				  }
				  
				  catch(IOException e){
					  try{
					      dos.close();
					      ois.close();
					      user_info_socket.close();
					      break;
					  }
					  
					  catch(Exception ee){
						  System.out.println("joining_server class�� joinInfo class, run() method���� "+ee+" �߻�!");
					  }					  
					  
				  }
				  
				  catch(ClassNotFoundException e){
					  					  
				  }
				  
			  }
			  
		  }
		 
	 }	//ȸ������ ������ �ְ� �޴� joiningInfo class ends//
	
}

