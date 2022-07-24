import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class client extends JFrame {
	
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;

	//declare components
	private JLabel heading = new JLabel("Client area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
	
	//Constructor
	public client()
	{
		try {
			
			System.out.println("sending request to server..");
			socket = new Socket("127.0.0.1",1110);
			System.out.println("Connection done..");
			
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream()); 
			
			createGUI();
			handleEvents();
			startReading();
			startWriting();
			
		} catch(Exception ec) {
			ec.printStackTrace();
		}
	}
	
	private void handleEvents() {
		
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == 10) {
					//System.out.println("you have pressed enter button");
					String contentToSend = messageInput.getText();
					messageArea.append("Me : " + contentToSend + "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
		});
	}

	private void createGUI()
	{
		//gui code
		this.setTitle("Client Messenger[END]");
		this.setSize(600,700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		//set frame layout
		this.setLayout(new BorderLayout());
		
		//adding the component
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jscrollpane = new JScrollPane(messageArea);
		this.add(jscrollpane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}	
	
public void startReading() {
		
		//thread reads the data
		Runnable r1 = ()->{
			
			System.out.println("reader started...");
			
			try {
			while(true) {
				
				
				String msg = br.readLine();
				if(msg.equals("exit")) {
					System.out.println("server terminated the chat");
					JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
					messageInput.setEnabled(false);
					socket.close();
					break;
				}
				
				//System.out.println("Server : " + msg);
				messageArea.append("Server : " + msg + "\n");
				
			}
			} catch(Exception ec) {
				System.out.println("Connection closed");
			}
		};
		
		new Thread(r1).start();
	}
	
	public void startWriting() {
		
		//thread - ask the data from the user and send it to the client
		Runnable r2 = ()->{
			
			System.out.println("writer started");
			
			try {
			while(!socket.isClosed()) {
				
					
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush(); 
				
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				
			}
				System.out.println("Connection closed");
			
			} catch(Exception ec) {
				ec.printStackTrace();
			}
		};
		
		new Thread(r2).start();
	}
	
	

	public static void main(String[] args) {
	
		System.out.println("this is client and start going");
		new client();
	}

}

