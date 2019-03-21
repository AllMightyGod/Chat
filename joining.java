package javachatting;

import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class joining extends JFrame{ //ȸ������ Ŭ���� ����//
	
	private JPanel contentPane;
	JLabel name_label,id_label,password_label,password_confirm_label,email_label,birth_label,sex_label;
	JLabel birth_yy_label,birth_mm_label,birth_dd_label;
	private JTextField name_txtField,id_txtField,email_txtField;
	private JTextField birth_yy_txtField, birth_mm_txtField, birth_dd_txtField;
	private JPasswordField password_Field,password_confirm_Field;
	private Choice sex_choice;
	JButton confirm_button;
	private String info_string[]=new String [6]; //ȸ������ ���� ���� �迭//
	
    private Socket socket; // �������
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    
    private String ip_address;
    private int check_result=-1;
	
    public joining(String str1){ //ȸ������ ����Ʈ���� ����//
    
    ip_address=str1;
    
    setVisible(true);
    setBounds(400, 200, 400, 392);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    setContentPane(contentPane);
    contentPane.setLayout(null); //�ڹ� ������ ������ �� ������Ʈ�� ������ �ʴ� ������ �ذ�!!//
    
    //�� ����� ����//
    
    name_label=new JLabel("�̸�");
    id_label=new JLabel("���̵�");
    password_label=new JLabel("��й�ȣ");
    password_confirm_label=new JLabel("��й�ȣ ���Է�");
    email_label=new JLabel("�̸��� �ּ�");
    birth_label=new JLabel("�������");
    sex_label=new JLabel("����");
    
    birth_yy_label=new JLabel("��");
    birth_mm_label=new JLabel("��");
    birth_dd_label=new JLabel("��");
    
    name_label.setHorizontalAlignment(JLabel.CENTER);
    id_label.setHorizontalAlignment(JLabel.CENTER);
    password_label.setHorizontalAlignment(JLabel.CENTER);
    password_confirm_label.setHorizontalAlignment(JLabel.CENTER);
    email_label.setHorizontalAlignment(JLabel.CENTER);
    birth_label.setHorizontalAlignment(JLabel.CENTER);
    sex_label.setHorizontalAlignment(JLabel.CENTER);
    
    name_label.setBounds(20,25,110,20);
    id_label.setBounds(20,55,110,20);
    password_label.setBounds(20,85,110,20);
    password_confirm_label.setBounds(20,115,110,20);
    email_label.setBounds(20,145,110,20);
    birth_label.setBounds(20,175,110,20);
    sex_label.setBounds(20,205,110,20);
    
    contentPane.add(name_label);
    contentPane.add(id_label);
    contentPane.add(password_label);
    contentPane.add(password_confirm_label);
    contentPane.add(email_label);
    contentPane.add(birth_label);
    contentPane.add(sex_label);
    
    //�� ��//
    
    name_txtField=new JTextField();
    id_txtField=new JTextField();
    password_Field=new JPasswordField();
    password_confirm_Field=new JPasswordField();
    email_txtField=new JTextField();
    birth_yy_txtField=new JTextField(4);
    birth_mm_txtField=new JTextField(2);
    birth_dd_txtField=new JTextField(2);
    sex_choice=new Choice();
    sex_choice.add("��");
    sex_choice.add("��");
    
    name_txtField.setBounds(150,25,200,20);
    id_txtField.setBounds(150,55,200,20);
    password_Field.setBounds(150,85,200,20);
    password_confirm_Field.setBounds(150,115,200,20);
    email_txtField.setBounds(150,145,200,20);
    birth_yy_txtField.setBounds(150,175,50,20);
    birth_yy_label.setBounds(205,175,20,20);
    birth_mm_txtField.setBounds(225,175,40,20);
    birth_mm_label.setBounds(270,175,20,20);
    birth_dd_txtField.setBounds(295,175,40,20);
    birth_dd_label.setBounds(340,175,40,20);
    sex_choice.setBounds(200,205,100,20);
    
    contentPane.add(name_txtField);
    contentPane.add(id_txtField);
    contentPane.add(password_Field);
    contentPane.add(password_confirm_Field);
    contentPane.add(email_txtField);
    contentPane.add(birth_yy_txtField);
    contentPane.add(birth_mm_txtField);
    contentPane.add(birth_dd_txtField);
    contentPane.add(birth_yy_label);
    contentPane.add(birth_mm_label);
    contentPane.add(birth_dd_label);
    contentPane.add(sex_choice);
    
    confirm_button=new JButton("Ȯ��");
    confirm_button.setBounds(130,270,150,40);
    contentPane.add(confirm_button);
    
    confirm_button.addActionListener(new ActionListener() { // Ȯ�� ��ư�� ������ �� event starts//
		   @Override
	   public void actionPerformed(ActionEvent e) { 	//actionPerformed starts//
	    if (name_txtField.getText().length()<2){
	    	JOptionPane.showMessageDialog(null, "�̸��� 2�� �̻� �Է����ּ���!");
	    	
	    } // �̸��� �Է����� ���� ��
	    
	    else if (name_txtField.getText().length()>10){
	    	JOptionPane.showMessageDialog(null, "�̸��� 10�� ���Ϸ� �Է����ּ���!");
	    	
	    } // �̸��� �Է����� ���� ��
	    
	    else if(id_txtField.getText().length()<6){
	    	JOptionPane.showMessageDialog(null, "id�� 6�� �̻� �Է����ּ���!");
	    }  // id�� �Է����� ���� ��
	    
	    else if(password_Field.getText().length()<6){
	    	JOptionPane.showMessageDialog(null, "��й�ȣ�� 6�� �̻� �Է����ּ���!");
	    }  // ��й�ȣ�� �Է����� ���� ��
	    
	    else if(password_confirm_Field.getText().equals("")){
	    	JOptionPane.showMessageDialog(null, "��й�ȣ�� Ȯ�����ּ���!");
	    }  // ��й�ȣ�� Ȯ������ ���� ��
	    
	    else if(!password_Field.getText().equals(password_confirm_Field.getText())){
	    	JOptionPane.showMessageDialog(null, "��й�ȣ�� ��ġ���� �ʽ��ϴ�!");
	    }  // ��й�ȣ�� ��ġ���� ���� ��
	    
	    else if(email_txtField.getText().equals("")){
	    	JOptionPane.showMessageDialog(null, "�̸��� �ּҸ� �Է����ּ���!");
	    }  // �̸��� �ּҸ� �Է����� ���� ��
 
	    else if(Integer.parseInt(birth_yy_txtField.getText())<1900||Integer.parseInt(birth_yy_txtField.getText())>2015){
	    	JOptionPane.showMessageDialog(null, "������ ����� �Է����ּ���!");
	    }  // ������ �Է����� ���� ��
	    
	    else if(Integer.parseInt(birth_mm_txtField.getText())<1||Integer.parseInt(birth_mm_txtField.getText())>13){
	    	JOptionPane.showMessageDialog(null, "������ ����� �Է����ּ���!");
	    	
	    }  // ������ �Է����� ���� ��
	    else if(Integer.parseInt(birth_dd_txtField.getText())<1||Integer.parseInt(birth_dd_txtField.getText())>32){
	    	JOptionPane.showMessageDialog(null, "������ ����� �Է����ּ���!");
	    }  // ������ �Է����� ���� ��
	    
	    else{
	    	String month="";
	    	String day="";
	    	info_string[0]=name_txtField.getText();
	    	info_string[1]=id_txtField.getText();
	    	info_string[2]=password_Field.getText();
	    	info_string[3]=email_txtField.getText();
	    	info_string[5]="";
	    	
	    	if(birth_mm_txtField.getText().length()==1){
	    		month="0"+birth_mm_txtField.getText();		
	    	}
	    	else if(birth_dd_txtField.getText().length()==1){
	    		day="0"+birth_dd_txtField.getText();
	    	}
	    	else{
	    		month=birth_mm_txtField.getText();
	    		day=birth_dd_txtField.getText();
	    	}
	    	info_string[4]=birth_yy_txtField.getText()+month+day;
	    	server();
	    	} // ������ ����
	   }
	  });  // Ȯ�� ��ư�� ������ �� event ends//
    }  //ȸ������ ����Ʈ���� ��//
    
    	public void server(){ //���� ���� starts, server starts//
    		try{
    			socket=new Socket(ip_address,8090);
    			server_network();
    		}
    		
    		catch(Exception e){System.out.println("server �޼ҵ忡�� �߻��� "+e);}
    		
    	} //server method ends//
    	
    	public void server_network(){ //server_network method starts//
    		try
    		{
    		user_info userinfo=new user_info(info_string[0],info_string[1],info_string[2],info_string[3],info_string[4],info_string[5]);
    		os=socket.getOutputStream();
    		oos=new ObjectOutputStream(os);
    		oos.writeObject(userinfo);
   		
  		  Thread th = new Thread(new Runnable() { //�����κ��� ������ ��ٸ��� �޼ҵ� ���� //
  			   @Override
  			   public void run() {
  			    while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
  			     try {
  		    		is=socket.getInputStream();
  		    		dis=new DataInputStream(is);
  		    		String result=dis.readUTF();
  		    		check_result=Integer.parseInt(result);
  		    		
  		    		if(check_result==1){JOptionPane.showMessageDialog(null, "������ ���ϵ帳�ϴ�!");
  		    		dispose();
  		    		oos.close();
  		    		dis.close();
  		    		os.close();
  		    		dis.close();
  		    		socket.close();
  		    		break;
  		    		}
  		    		
  		    		else if(check_result==2){JOptionPane.showMessageDialog(null, "�̹� ��� ���� ���̵� �Դϴ�. �ٸ� ���̵� �Է����ּ���!");
  		    		id_txtField.setText("");
  		    		id_txtField.requestFocus();
  		    		}
  		    		
  		    		else if(check_result==-1){
  		    		JOptionPane.showMessageDialog(null, "ȸ�� ���� ������ ������ ������ϴ�!");
  		    		dispose();
  		    		}
  		    		
  			     } 

  			     	catch (Exception e) {
  	  			     	JOptionPane.showMessageDialog(null, "ȸ�� ���Կ� �����߽��ϴ�! �ٽ� �� �� �õ��� �ּ���!");
  	  			     	dispose();
  			    	 break;
  			     }
  			    }
  			   }
  			  });//�����κ��� ������ ��ٸ��� �޼ҵ� ��
  		  
  		  	th.start();
  		  
    		}
    		
    		catch(Exception e){System.out.println("server_network���� �Ͼ "+e);}
    	}//server_network method ends//
    
}  //ȸ������ Ŭ���� ��//

