import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;


public class Server  {

	ServerSocket servSock;
	private Hashtable outputStreams;
	//Constructor and while-accept loop all in one.
	public Server(int port) throws IOException{
		
		outputStreams = new Hashtable();
		// listen after connections
		listen(port);
		
	}
	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt("111");
		new Server(port);
	}
	
	private void listen(int port) throws IOException{
		
		// Skapa serversocket
		servSock = new ServerSocket(port);
		
		//"Servern är redo"
		System.out.println("Listening on" + servSock);
		
		//Acceptera connections kontinuerligt
		while(true){
			
			//Fånga upp nästa inkommande connection
			Socket sock = servSock.accept();
			
			//Berätta att connectionen är fångad
			System.out.println("Connection from " +sock);
			
			//Skapa en data-ut-ström för att skriva ut data
			DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
			
			//Spara strömmen so vi inte gör den igen
			outputStreams.put(sock,dout);
			
			//Skapa en ny tråd för anslutningen, och glöm den
			new ServerThread(this, sock);
		}
	}






	//Get an enumeration of all the OutputStreams, one for each client
	//connected to us
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}
	//Send a message to all clients (utility routine)
	void sendToAll( String message ) {
		//We synchronize on this because another thread might be
		//calling removeConnection() and this would screw us up
		//as we tried to walk through the list
		synchronized( outputStreams ) {
			//For each client ...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
				//... get the output stream ...
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				//... and send the message
				try {
					dout.writeUTF( message );
				} catch( IOException ie ) { System.out.println( ie ); }
			}
		}
	}
	//Remove a socket, and it's corresponding output stream, from our
	//list. This is usually called by a connection thread that has
	//discovered that the connectin to the client is dead.
	void removeConnection( Socket s ) {
		//Synchronize so we don't mess up sendToAll() while it walks
		//down the list of all output streamsa
		synchronized( outputStreams ) {
			//Tell the world
			System.out.println( "Removing connection to "+s );
			//Remove it from our hashtable/list
			outputStreams.remove( s );
			//Make sure it's closed
			try {
				s.close();
			} catch( IOException ie ) {
				System.out.println( "Error closing "+s );
				ie.printStackTrace();
}
}
}
}
