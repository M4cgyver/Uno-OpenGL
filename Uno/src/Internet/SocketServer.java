package Internet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//This will be the class that will hold all the socket infomation
public class SocketServer {
	public static interface SocketServerInterface { void main(Socket s, Object o); public void connect(StructClient s);}
	
	public class StructClient
	{ 
		public ObjectOutputStream 					ooStream;
		public ObjectInputStream 					oiStream;
		public Socket 								socket;
		public Thread 								tListener; 
		public SocketServer.SocketServerInterface 	ssi;
		
		public StructClient(Socket s, SocketServer.SocketServerInterface ssi) throws IOException
		{
			this.socket = s;														//Set the socket
			oiStream 	= new ObjectInputStream(socket.getInputStream());			//Get the input stream
			ooStream 	= new ObjectOutputStream(socket.getOutputStream());			//Get the output stream 
			
			Runnable rListener = () ->												//Create a runnable class to get input
			{
				while(true)
				{
					try {
						Object message = oiStream.readObject();						//Try and read the object
						ssi.main(socket, message);									//Pass the message
						
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			tListener = new Thread(rListener);
			tListener.start();
		}
		
		public void send(Object o) throws IOException
		{
			ooStream.writeObject(o);
		}
	}

	
	private ServerSocket server;
	public final static int _port = 3656;
	private ArrayList<StructClient> clients;
	private Thread tListener; 
	
	public SocketServer(SocketServerInterface ssi) throws IOException
	{
		System.out.println("Initalizing Server " + this.toString() + "...");	//Debug outout
		
		server = new ServerSocket(_port);
		clients = new ArrayList<StructClient>(); 
		
		System.out.println("Setting up threads...");
		Runnable rListener = () ->												//Set the runnable listener 
		{
			System.out.println("Listening...");
			while(true)															//Have it to always run
			{
				try {
					Socket socket = null;										//Accept the socket and add to list
					socket = server.accept(); 
					clients.add(new StructClient(socket, ssi));
					System.out.println("Server Connected!");
					ssi.connect(clients.get(clients.size()-1));
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		};
		
		tListener = new Thread(rListener);										//Setup the thread as runnable
		
		System.out.println(this.toString() + " done!");
	}
	
	public void run() throws IOException
	{
		tListener.start();														//Start the listening thread
	}
	
	public void send(Object o) throws IOException
	{
		for(StructClient c : clients) c.ooStream.writeObject(o);
	}
	
	public void close() throws IOException
	{
		//tListener.stop();
		
		server.close();
		
		for(StructClient c : clients)
		{
			c.socket.close();
			//c.tListener.stop();
		}
		
		clients.clear();
	}

	public ServerSocket getServer() {
		return server;
	}

	public int get_port() {
		return _port;
	}

	public ArrayList<StructClient> getClients() {
		return clients;
	}
}
