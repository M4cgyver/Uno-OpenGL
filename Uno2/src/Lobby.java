import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Internet.SocketClient;
import Internet.SocketServer;
import Internet.SocketServer.StructClient; 

//The lobby class is what holds the players, deck, deck offset, etc
public class Lobby {
	public enum PHASE
	{
		LOBBY, GAME
	};
	
	//Main variables and such
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static String color;
	public static int index = 0;
	public static int offset = 1; 
	public static PHASE phCurrent = PHASE.LOBBY;
	
	//Internet sockets
	public static SocketServer sServer = null;
	public static SocketClient sClient = null;
	
	public static void reset() {
		//Set not only all the players but the deck as a clean state
		for (Player p : players)
			p.hand.clear();
		
		Deck.reset();

		//Setup the hand for the lobby
		for (Player p : players)
			playerReset(p);
		
	}

	public static void createLobby() throws IOException
	{
		System.out.println("Resetting the deck...");
		reset();
		
		SocketServer.SocketServerInterface ssi = new SocketServer.SocketServerInterface() 
		{
			@Override
			public void recieve(Socket s, Object o) {
				try {	
					int sIndex = sServer.getSocketIndex(s);
					if(phCurrent.equals(PHASE.LOBBY))														//if we are still in the lobby
					{
						sServer.send(new String("Player " + (sIndex+1) + ": " + (String) o));
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}

			@Override
			public void connect(SocketServer.StructClient s) {
				try {																						//Attempt to send all the players the new player instance

					for(Player p : players)
						s.send(p);
					
					Player p = new Player();																//Add the player to the list
					playerReset(p);																			//Reset the players hand
					
					
					sServer.send(p);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void disconnect(StructClient s) {
				int sIndex = sServer.getSocketIndex(s.socket);												//Get the index of the connection socket
				System.out.println("Update! Player " + (sIndex+1) + " has disconnected!");					//Print the player has disconnected
				sServer.getClients().remove(s);																//Remove the client from the list
				players.remove(sIndex);																		//Remove the client from the player list
				try {
					sServer.send(players);																	//Attempt to send the updated player list
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};
		
		sServer = new SocketServer(ssi);
		sServer.run();
	}
	
	public static void closeLobby() throws IOException
	{
		sServer.close();																					//Attempt to close the server
		sServer = null;																						//Null the previous iteration so it will be deleted by the garbage collector
	}

	public static void joinLobby(String host, int port) throws IOException, SocketTimeoutException, ConnectException, UnknownHostException
	{
		//Setup the clients 
		if(sClient != null)																					//Initial client setup
		{
			sClient.close();																				//Close the clients if there is a client
		}
		
		SocketClient.SocketClientInterface sci = new SocketClient.SocketClientInterface() 					//Next setup the interface for message recieve
		{
			public void main(Object o) 
			{																								//Now that we have recieved data, we need to know what kind of data it is
				if(o.getClass() == String.class)															//Is it a normal string? print
					System.out.println((String) o);
				else if(o.getClass() == Player.class)														//Is it a new player? Save and add the player
				{
					players.add((Player) o);
					System.out.println("New player joined! Total players: " + players.size());
				}
				else if(o.getClass() ==  players.getClass())												//Is it an array of players? Update the player array
					players = (ArrayList<Player>)o;
				
				else if(o.getClass() == Deck.Data.class)													//Is it deck data? Update the deck
					Deck.data = (Deck.Data) o;
				
			}

			@Override
			public void disconnect() {
				System.out.println("Disconected from the server! Leaving...");
				System.exit(0);
			}
		};
		
		sClient = new SocketClient(host, port, sci);														//Attempt to create and connect to server
		
	}
	
	public static void playerReset(Player p)
	{
		for (int i = 0; i < 7; i++)
			p.hand.add(Deck.take((int) (Math.random() * Deck.size())));
	}
	
	public static void playerDraw(int index, int cards) {
		for (int j = 0; j < cards; j++)
			players.get(index).hand.add((Deck.take((int) (Math.random() * Deck.size()))));
	}

	public static void playerDraw(Player p, int cards) {
		for (int j = 0; j < cards; j++)
			p.hand.add((Deck.take((int) (Math.random() * Deck.size()))));
	}

	public static long getPlayerTurn() {
		return Math.abs(index % players.size());
	}

	public static long getPlayerTurn(long i) {
		return Math.abs(i % players.size());
	}
}