package javachatting;


import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class server extends JFrame{				//server Ŭ���� ����//
	
	 private JTextField chatting_textField; 			// ä���Է�â
	 private JButton inserting_Button; 				// �Է¹�ư
	 JTextArea chatting_textArea; 					// ä��â
	 JTextArea chatting_member_textArea; 				// �� ������ �ο� üũ
	 private ServerSocket server_socket; 				// ä�� ��������
	 private Socket chatting_text_socket; 				// ä�ü���
	 private Vector vc = new Vector(); 				// ����� ����ڸ� ������ ����

	 private ServerSocket user_list_server_socket; 				
	 private Socket user_list_socket; 				

	 
	 public server() { 						// GUI�� �����ϴ� server constructor starts//
		 
		  try{ 								//try starts//
		  System.out.println("server starting...");															
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  setVisible(true);
		  setSize(400,400);
	  	  setLayout(null);
		  setLocation(700,200);						
	  
		  JScrollPane JScroll_bar = new JScrollPane();
		  chatting_textArea = new JTextArea();
		  chatting_textArea.setColumns(1);
		  chatting_textArea.setRows(5);
		  JScroll_bar.setBounds(10, 10, 260, 300);
		  JScroll_bar.setViewportView(chatting_textArea);
		  chatting_textArea.setEditable(false); 																							
		  add(JScroll_bar);
		  
		  chatting_textField = new JTextField();
		  chatting_textField.setBounds(10, 320, 260, 40);
		  chatting_textField.setColumns(10);
		  chatting_textField.setEditable(true);																		
		  add(chatting_textField);
		  
		  JScrollPane member_JScroll_bar = new JScrollPane();
		  chatting_member_textArea = new JTextArea();
		  chatting_member_textArea.setColumns(1);
		  chatting_member_textArea.setRows(5);
		  member_JScroll_bar.setBounds(280, 10, 100, 300);
		  member_JScroll_bar.setViewportView(chatting_member_textArea);															
		  chatting_member_textArea.setEditable(false);
		  add(member_JScroll_bar);

		  inserting_Button = new JButton("�Է�");
		  inserting_Button.setBounds(280, 320, 100, 40);
		  add(inserting_Button);
		  
		  log_in_server log_in_server=new log_in_server();
		  joining_server joining_server=new joining_server(); 			//ȸ������ ���� ����//
		  server_start();
		  } 								//try ends//
		  
		  catch(Exception e){}
		  
	     } 								//server constructor ends//

	 
	 private void server_start() { 					//server_start method starts//
		 
		  try {								//try ����
		   
		   server_socket = new ServerSocket(8080); 				// ������ ��Ʈ ���ºκ�
		   user_list_server_socket = new ServerSocket(8081);
		   
		   if(server_socket!=null	&&	user_list_server_socket!=null) 						// server socket�� ���������� ��������		
		   {
		  	Thread th = new Thread(new Runnable() { 				// ����� ������ ���� ������
			@Override
		        
				public void run() {

		   		while (true) { 								// ����� ������ ����ؼ� �ޱ� ���� while��
					try {
							if(vc.size()==0){
								 chatting_textArea.append("����� ������ ��ٸ��� ��...\n");
							}
						
								 chatting_text_socket = server_socket.accept();
								 user_list_socket = user_list_server_socket.accept();
								 UserInfo user = new UserInfo(chatting_text_socket, user_list_socket,vc);// ����� ���� ������ �ݹ� ������Ƿ�, user Ŭ���� ���·� ��ü ����
                                                                 						// �Ű������� ���� ����� ���ϰ�, ���͸� ��Ƶд�
								 vc.add(user); 						// �ش� ���Ϳ� ����� ��ü�� �߰�
								 user.start(); 						// ���� ��ü�� ������ ����
							 
					     } catch (IOException e) {
				        	chatting_textArea.append("!!!! accept ���� �߻�... !!!!\n");
					     }
		  		     }
			    }
		  });

			  th.start();
		   }								
		  }								//try ��
 
	         catch (IOException e) {
			  System.out.println("������ �̹� ������Դϴ�...");
			  System.exit(0);
		  }
		 } 							//server_start method ends//
	 
	 
	 
	 class UserInfo extends Thread { 				//����Ŭ���� UserInfo Ŭ���� ����//
		  private InputStream is;
		  private OutputStream os, list_os;			//ä�� ������ ������ ��Ʈ���� ä�� ��� ����Ʈ�� ������ ��Ʈ��//
		  private DataInputStream dis;
		  private DataOutputStream dos, list_dos;					//ä�� ������ ������ ������ ��Ʈ��//
		  private Socket user_socket, list_socket; 					//ä�� ������ �ְ� �޴� ����//
		  private Vector user_vc;
      	  private String id = "";

		  public UserInfo(Socket soc1, Socket soc2, Vector vc) 			// �����ڸ޼ҵ�
		  {
		   									// �Ű������� �Ѿ�� �ڷ� ����
		   this.user_socket = soc1;
		   this.list_socket = soc2;
		   this.user_vc = vc;
		   User_network();
		  }

		  public void User_network() { 					//User_network method starts//
		   try {
		    is = user_socket.getInputStream();
		    dis = new DataInputStream(is);
		    os = user_socket.getOutputStream();
		    dos = new DataOutputStream(os);
		    id = dis.readUTF(); 						// ������� �г��� �޴ºκ�

		    list_os = list_socket.getOutputStream();
		    list_dos = new DataOutputStream(list_os);

		    chatting_textArea.append("������ ID " +id+"\n");
		    chatting_member_textArea.append(id+"\n");
			   try {
				    dos.writeUTF("���� ���� �Ǿ����ϴ�.");
   				    for (int i = 0; i < user_vc.size(); i++) {
   				    		UserInfo imsi = (UserInfo) user_vc.elementAt(i);
   				    		imsi.list_dos.writeUTF(id);
			   				list_dos.writeUTF(imsi.id);
			   			}
				   } 
				   catch (IOException e) {
					   chatting_textArea.append("�޽��� �۽� ���� �߻�\n"); 
				   }							// ����� ����ڿ��� ���������� �˸�
		    inserting_Button.addActionListener(new ActionListener() { 
			   @Override
			   public void actionPerformed(ActionEvent e) { 	//ä��â �Է� �̺�Ʈ ����//
			    if (chatting_textField.getText().equals("") || chatting_textField.getText().length()==0){					
				    chatting_textField.requestFocus();
			    	} 							//ä��â�� �ƹ� �͵� �Է����� ���� ���� ��ư�� ������ �޼����� �������� �ʰ�
	
			    else{																
			    	try
			    	{
	    				
			   			try {						//������ �޼����� Ŭ���̾�Ʈ�鿡�� ������ ����//

			   				    for (int i = 0; i < user_vc.size(); i++) {
			   				    	UserInfo imsi = (UserInfo) user_vc.elementAt(i);
			   				    	imsi.dos.writeUTF("��ڴԲ��� "+chatting_textField.getText()+"��� �Ͻʴϴ�.");
			   				    }
			   				    
			   			} 						//������ �޼����� Ŭ���̾�Ʈ�鿡�� ������ ��//

			   			catch (IOException ee) {		//������ �޼����� Ŭ���̾�Ʈ�鿡�� ������ ���� �� ����//
				  			chatting_textArea.append("�޽��� �۽� ���� �߻�\n"); 
			   			}					//������ �޼����� Ŭ���̾�Ʈ�鿡�� ������ ���� �� ��//
			    			
			    		}
			    		catch(Exception er)
			    		{
			    			System.out.println(er);
			    		}
			    	
    					chatting_textArea.append("��ڴԲ��� "+chatting_textField.getText()+"��� �Ͻʴϴ�."+"\n");
    					chatting_textField.setText("");
    					chatting_textField.requestFocus();		// Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
    					
				    }							//ä��â�� �Է��� �޼����� Ŭ���̾�Ʈ���� ���� ��
				   }
				  }); 						// ä��â �Է� �̺�Ʈ ��
		    
		   } catch (Exception e) {
			   chatting_textArea.append("��Ʈ�� ���� ����\n");
		   }
		  }								//User_network method ends//
  
		  
		  public void run() 						// Ŭ���̾�Ʈ�κ��� �޼����� �����ϰ� �ٸ� Ŭ���̾�Ʈ�鿡�� ������ run()�޼ҵ� ����
		  {
		   while (true) {
		    try {	     							// ����ڿ��� �޴� �޼���
		     String msg = dis.readUTF();
		     chatting_textArea.append(id+"�κ��� ���� �޼��� : " + msg+"\n");		// ����ڷκ��� ���� �޼����� ���� ȭ�鿡 ǥ��
		     for (int i = 0; i < user_vc.size(); i++) {					//Ŭ���̾�Ʈ�κ��� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�� ���� ����
		     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
		     try {
		     		imsi.dos.writeUTF(id+" : "+msg);
		     	}

		     	catch (IOException e) {
		     		chatting_textArea.append("�޽��� �۽� ���� �߻�\n"); 
		     	}
		      }
		    }										//Ŭ���̾�Ʈ�κ��� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�� ���� ��

		    catch (IOException e)						//Ŭ���̾�Ʈ�� ������ ���� �� ���� ó�� ����
		    {
		     
		     try {									//�� �ǹ� ���� try�� ����
		      dos.close();
		      dis.close();
		      user_socket.close();
		      vc.removeElement( this ); 							// �������� ���� ��ü�� ���Ϳ��� �����
		      chatting_textArea.append(vc.size() +" : ���� ���Ϳ� ����� ����� ��\n");
		      chatting_textArea.append("����� ���� ������ �ڿ� �ݳ�\n");
		      chatting_member_textArea.setText("");
		      String accessing_id="";
		      
			   for (int i = 0; i < user_vc.size(); i++) {
				    UserInfo imsi = (UserInfo) user_vc.elementAt(i);
  				    accessing_id=imsi.id;
				    chatting_member_textArea.append(accessing_id+"\n");
				   }			   //ä�ù� ���� ������ ����Ʈ�� ������Ʈ�Ѵ�.
			   
			    for (int i = 0; i < user_vc.size(); i++) {					//Ŭ���̾�Ʈ�κ��� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�� ���� ����
				     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
				     imsi.list_dos.writeUTF("deleteallinchattingmembertextarea");}
			    
			    for (int i = 0; i < user_vc.size(); i++) {					//Ŭ���̾�Ʈ�κ��� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�� ���� ����
				     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
					    for (int j = 0; j < user_vc.size(); j++) {					//Ŭ���̾�Ʈ�κ��� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�� ���� ����
						     UserInfo list = (UserInfo) user_vc.elementAt(j);
						     if(imsi.id.equals(list.id)){}
						     else{
						     imsi.list_dos.writeUTF(list.id);}
						     }
						    }
			   
		      break;
		     
		     } catch (Exception ee) {							//�� �ǹ� ���� try�� ��, ���������� �� �ǹ� ���� catch�� ����
		     
		     }										//�� �ǹ� ���� catch�� ��
		    }									//Ŭ���̾�Ʈ�� ������ ���� �� ���� ó�� ��
		   }
		  }								//run�޼ҵ� ��
		 } 							// ���� Ŭ���� userInfo Ŭ���� ��//
	 
	public static void main(String args[]){			//main method starts//

			server server=new server();
			server.setVisible(true);

	}							//main method ends//
}
