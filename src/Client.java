import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.*;




public class Client extends JFrame implements Runnable {	
	
	private TextField tf;
	private TextArea ta;
	private DataInputStream din;
	private DataOutputStream dout;
	public Client(String host, int port) {

		ta = new TextArea();
		tf = new TextField();
		//Set up the screen

		setLayout(new BorderLayout());
		add("North", tf);
		add("Center", ta);
		setSize(400,400);
		setVisible(true);
		//Ta emot medellanden när någon skriver och trycker på enter
		
		tf.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e ) 
			{
				
			processMessage( e.getActionCommand());
			}
			} );
			
			
		
		// Anslut till servern
		try {
		// Initiera anslutningen
		Socket socket = new Socket( host, port );
		// Anslutningen lyckades, skriv ut det!
		System.out.println( "connected to "+socket );
		// skapa in och ut strömmar
		din = new DataInputStream( socket.getInputStream() );
		dout = new DataOutputStream( socket.getOutputStream() );
		// Tråd för att ta emot medellandens
		new Thread( this ).start();
		} catch( IOException ie ) { System.out.println( ie );} }
		
		
		// Gets called when the user types something
		private void processMessage( String message ) {
		try {
		// Send it to the server
		dout.writeUTF( message );
		// Clear out text input field
		tf.setText( "" );
		} catch( IOException ie ) { System.out.println( ie ); }
		}
			
		// Background thread runs this: show messages from other window
	@Override
	public void run() {

		try {
		// Receive messages one-by-one, forever
		while (true) {
		// Get the next message
		String message = din.readUTF();
		// Print it to our text window
		ta.append( message+"\n" );
		}
		} catch( IOException ie ) { System.out.println( ie ); }
		}
		
	
	public static void main(String[] args) {
		Client client = new Client("127.0.0.1",111);
	}
		}
	


	
	


