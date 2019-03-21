package javachatting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class ChattingView extends JFrame {  // ä�� ȭ�� ChattingView class starts  //
	 private JPanel contentPane;
	 private JTextField chatting_textField; // ���� �޼��� ���°�
	 private String id;
	 private String password;
	 private String ip;
	 private int port=8080;
	 JButton send_button; // ���۹�ư
	 JTextArea chatting_textArea,chatting_member_textArea;
	 private Socket socket; // �������
	 private Socket user_list_socket;	// ���� ��� ����Ʈ�� �޾Ƶ��̴� ����
	 private InputStream is;
	 private OutputStream os;
	 private DataInputStream dis;
	 private DataOutputStream dos;
	 private InputStream user_list_is;	//���� ����� ����Ʈ�� �޾Ƶ��̴� ������ ��Ʈ��
	 private DataInputStream user_list_dis;
	 private OutputStream user_list_os;	//���� ��ȣ�� ������ ������ ��Ʈ��
	 private DataOutputStream user_list_dos;
	 
	 public ChattingView(String id, String password, String ip){  // ChattingView constructor starts//
	  super(id+"���� ��ȭâ�Դϴ�.");
	  this.id = id;
	  this.password=password;
	  this.ip=ip;
	  
	  
	  init();
	  chatting_textArea.append("ä�ù濡 �����Ͽ����ϴ�." + "\n");
	  chatting_member_textArea.append("��" + "\n");
	  network();
	 }// ChattingView constructor ends//
	 
	 
	 public void network() {  // ������ ����

	try {
	   socket = new Socket(ip, port);
	   user_list_socket = new Socket(ip, 8081);
	   if (socket != null	&&	user_list_socket != null) // socket�� null���� �ƴҶ�, ����Ǿ�����
	   {
	    Connection(); // ���� �޼ҵ带 ȣ��
	   }
	  } catch (UnknownHostException e) {
	  } catch (IOException e) {
		  chatting_textArea.append("���� ���� ����!!\n");
	  }
	 }
	 
	 public void Connection() { // ���� ���� �޼ҵ� ����κ�
	  try { // ��Ʈ�� ����
	   is = socket.getInputStream();
	   dis = new DataInputStream(is);
	   os = socket.getOutputStream();
	   dos = new DataOutputStream(os);
	   user_list_os = user_list_socket.getOutputStream();
	   user_list_dos = new DataOutputStream(user_list_os);
	   user_list_is = user_list_socket.getInputStream();
	   user_list_dis = new DataInputStream(user_list_is);

	   
	  } catch (IOException e) {
		  chatting_textArea.append("��Ʈ�� ���� ����!!\n");
	  }
	  
	  send_Message(id); // ���������� ����Ǹ� id�� ����
	  
	  Thread th = new Thread(new Runnable() { // �����带 ������ �����κ��� �޼����� ����
	     @Override
	     public void run() {
	      while (true) {
	       try {
	        String msg = dis.readUTF(); // �޼����� �����Ѵ�
	        chatting_textArea.append(msg + "\n");
	       } catch (IOException e) {
	    	   chatting_textArea.append("������ ������ ������ϴ�!!!\n");
	        // ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
	        try {
	         os.close();
	         is.close();
	         dos.close();
	         dis.close();
	         socket.close();
	         
		   
	         break; // ���� �߻��ϸ� while�� ����
	        } catch (IOException e1) {
	        }
	       }
	      } // while�� ��
	     }// run�޼ҵ� ��
	    });
	  
	  Thread list_th = new Thread(new Runnable() { // �����带 ������ �����κ��� ä�� ���� ����Ʈ�� ����
		     @Override
		     public void run() {
		      while (true) {
		       try {
		    	String user_list=user_list_dis.readUTF();
		    	if(user_list.equals("deleteallinchattingmembertextarea")){
		    	chatting_member_textArea.setText("");
		  	  	chatting_member_textArea.append("��" + "\n");
		    	}
		    	else{
	        	chatting_member_textArea.append(user_list+"\n");
		    	}
		       } catch (IOException e) {
		    	   chatting_textArea.append("���� ����Ʈ ���� ����!!\n");		    	   
		        // ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
		        try {
			  	     user_list_dos.close();
		        	 user_list_os.close();
			  	     user_list_dis.close();
		        	 user_list_is.close();
			  	   	 user_list_socket.close();
			   
		         break; // ���� �߻��ϸ� while�� ����
		        } catch (IOException e1) {
		        }
		       }
		      } // while�� ��
		     }// run�޼ҵ� ��
		    });
	  
	  th.start();
	  list_th.start();
	 }
	 
	 public void send_Message(String str) { // ������ �޼����� ������ �޼ҵ�
	  try {
	   dos.writeUTF(str);
	  } catch (IOException e) {
		  chatting_textArea.append("�޼��� �۽� ����!!\n");
	  }
	 }
	 
	 public void init() { // GUI�� �����ϴ� init() method starts//
	  
	  setVisible(true);
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  setBounds(700, 200, 400, 400);
	  contentPane = new JPanel();
	  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	  setContentPane(contentPane);
	  contentPane.setLayout(null);
	  
	  chatting_textArea = new JTextArea();
	  chatting_textArea.setEditable(false);
	  chatting_textArea.setColumns(1);
	  chatting_textArea.setRows(5);
	  JScrollPane Jscrollbar = new JScrollPane();
	  Jscrollbar.setBounds(10, 10, 260, 300);
	  Jscrollbar.setViewportView(chatting_textArea);
	  contentPane.add(Jscrollbar);
	  
	  JScrollPane member_JScroll_bar = new JScrollPane();
	  chatting_member_textArea = new JTextArea();
	  chatting_member_textArea.setColumns(1);
	  chatting_member_textArea.setRows(5);
	  member_JScroll_bar.setBounds(280, 10, 100, 300);
	  member_JScroll_bar.setViewportView(chatting_member_textArea);
	  chatting_member_textArea.setEditable(false); // textArea�� ����ڰ� ���� ���ϰԲ� ���´�.
	  contentPane.add(member_JScroll_bar);
	  	  
	  chatting_textField = new JTextField();
	  chatting_textField.setBounds(10, 320, 260, 40);
	  chatting_textField.setColumns(10);
	  contentPane.add(chatting_textField);
	  
	  send_button = new JButton("��   ��");
	  send_button.setBounds(280, 320, 100, 40);
	  send_button.addActionListener(new Myaction());
	  contentPane.add(send_button);

	 } //init() method ends//

	 class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	 {
	  @Override
	  public void actionPerformed(ActionEvent e) {
	   if (e.getSource() == send_button) // ������ ��ư�� ���� ��
	   {
		if(chatting_textField.getText().equals("")){
		    chatting_textField.requestFocus(); 
	   	}//�޼����� �Է����� �ʰ� ������ ��ư�� ������ ���ڰ� ���۵��� �ʴ´�.
	   
		else{
		    send_Message(chatting_textField.getText());
		    chatting_textField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
		    chatting_textField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
		}
	   
	   }
	  }
	 }
	} // ChattingView class ends //


