
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class server extends JFrame {
	
	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	//declare components
		private JLabel heading = new JLabel("Server area");
		private JTextArea messageArea = new JTextArea();
		private JTextField messageInput = new JTextField();
		private Font font = new Font("Roboto",Font.PLAIN,20);
		
	
	//Constructor
	public server() {
		
		try {
			server = new ServerSocket(1110);
			System.out.println("Server is ready to accept connection");
			System.out.println("waiting....");
			socket = server.accept();
			
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
		// TODO Auto-generated method stub
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

	private void createGUI() {

		//gui code
				this.setTitle("Server Messenger[END]");
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
					System.out.println("client terminated the chat");
					JOptionPane.showMessageDialog(this, "Client Terminated the Chat");
					messageInput.setEnabled(false);
					socket.close();
					
					break;
				}
				
				//System.out.println("Client : " + msg);
				messageArea.append("Client : " + msg + "\n");
				
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
			} catch(Exception ec) {
				System.out.println("Connection closed");
			}

		};
		
		new Thread(r2).start();
	}
	

	public static void main(String[] args) {

		System.out.println("this is server and going to start server");
		new server();
	}

}


