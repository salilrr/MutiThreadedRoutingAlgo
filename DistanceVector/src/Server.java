

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * 
 * Class Server
 * Class that constantly listens on the port for new connections.
 */
public class Server extends Thread  {
	
	int portNumber;
	String ip;
	String subnetMask="255.255.255.0";
	public Server(int port,String ip)
	{
		this.portNumber=port;
		this.ip=ip;
	}
	
	@SuppressWarnings("resource")
	public void run()
	{
	try {
		
		ServerSocket listen=new ServerSocket(portNumber);
	
		while(true)
		{
			Socket client=listen.accept();//listening on the port for new connection.
			ServerConnection serverCon=new ServerConnection(client,portNumber);//creates a new ServerConnection object to handle the newly connected Router
			serverCon.start();
		}
		
		
	} catch (IOException e) {
		System.out.println("Io Exception");
	}
	
	}

}
