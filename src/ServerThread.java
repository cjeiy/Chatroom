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
		
		//Starta tr�den
		start();
	}

	//k�rs i en seoarat tr�d n�r start() �r kallat
	
	public void run(){
		try {
			//skapa en in-str�m f�r att kommunicera, klienten kommer anv�nda en ut-str�m
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			
			while(true){
				
				// l�s n�sta medellande / readUTF returnerar en unicode str�ng
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
			//anslutningen st�ngs av n�gon anledning, servern tar hand om det
			server.removeConnection(socket);
		}
	}
}
