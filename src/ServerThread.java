import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;


public class ServerThread extends Thread {

	private Server server;
	private Socket socket;

	public ServerThread(Server server, Socket socket){
		
		this.server = server;
		this.socket = socket;
		
		//Starta tråden
		start();
	}

	//körs i en seoarat tråd när start() är kallat
	
	public void run(){
		try {
			//skapa en in-ström för att kommunicera, klienten kommer använda en ut-ström
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			
			while(true){
				
				// läs nästa medellande / readUTF returnerar en unicode sträng
				String message = dataIn.readUTF();
				
				//logga medellandet
				System.out.println("Sending "+message);
				
				//skicka till alla klienter
				
				server.sendToAll(message);
				
			}
		} catch (EOFException eofException) {
			
		} catch(IOException ioException){
			ioException.printStackTrace();
		} finally{
			//anslutningen stängs av någon anledning, servern tar hand om det
			server.removeConnection(socket);
		}
	}
}
