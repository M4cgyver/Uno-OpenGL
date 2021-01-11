package Internet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SocketClient {
	public static interface SocketClientInterface { void main(Object o); void disconnect();}		//Setup the interface to call the funciton
	
    ObjectOutputStream ooSocket 	= null;
    ObjectInputStream oiSocket 		= null;
	Socket socket 					= null;
	InetAddress address 			= null;
	Thread tListener				= null;
	SocketClientInterface sci		= null;
	boolean bOffset					= false;
	 
	public SocketClient(String host, int port, SocketClientInterface sci) throws IOException, SocketTimeoutException, ConnectException, UnknownHostException
	{
		System.out.println("Initalizing Client " + this.toString() + "...");	//Debug outout

		System.out.print("Status: ");											//Check if server is online
        this.address = InetAddress.getByName(host);								//Get address
        boolean reachable = address.isReachable(10000);	
        if(reachable){
            System.out.println("Online");										//Print online
        }
        else{
            System.out.println("Offline");
            throw new SocketTimeoutException();
        }
        
        System.out.println("Connecting...");
        this.socket = new Socket(address, port);								//Setup the socket
        System.out.println("Connected!");
        ooSocket = new ObjectOutputStream(socket.getOutputStream());
        oiSocket = new ObjectInputStream(socket.getInputStream());
        this.sci = sci;
        
        System.out.println("Client Connected!");

        Runnable rListener = () ->												//Create a runnable class to get input
		{
			System.out.println(this.toString() + " Listening...");
			while(true)
			{ 
				try {
					Object message = oiSocket.readObject();						//Try and read the object 
					sci.main(message);											//Pass the message 
					bOffset = !bOffset;											//Flip the offset 

				} catch (SocketException e)										//Thrown when someone disconnects	
				{
					sci.disconnect();												//Pass when someome has disconnected
					break;															//Break out of the loop
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		tListener = new Thread(rListener);
		tListener.start();
		System.out.println(this.toString() + " done!");
	}
	
	public void send(String s) throws IOException
	{ 
		ooSocket.writeObject(s);
	}

	public void sendAndWait(String s) throws IOException, InterruptedException
	{ 
		boolean bOldOffset = bOffset;
		ooSocket.writeObject(s);
		
		while(bOldOffset == bOffset);
	}
	
	public void close() throws IOException
	{
		socket.close();
		//tListener.stop();
	}
}
