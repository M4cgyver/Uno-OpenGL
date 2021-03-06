import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import Internet.SocketClient;
import Internet.SocketServer;

/*=====================================================*/
/* Desription: This is the card class, this just holds
 * data in order to figure out what card is either in
 * the deck or is in the players hand, only holds basic
 * values and the getters/setters
/*=====================================================*/
abstract class Card implements Serializable{
	private static final long serialVersionUID = 5051683348763987382L;
	public static String[] Strings = { "red", "blue", "green", "yellow" }; // Incase for whatever reason we wanted to add more
	protected String color; // Note: This is a color object. Java has predefined libraries for colors, so we
							// can use them to make custom collors if needed
	protected int value;

	// Basic getters and setters
	public Card(String color, int value) {
		this.color = color;
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setString(String color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// Check if card is a match
	public boolean isMatch(Card c) {
		return color.equals(c.color) && value == c.getValue();
	}

	// Abstract functions for later use
	public abstract void hitPlayer(Player p); // Sometimes we have a special card (+4, +2, etc) and we want to do
												// something special to the player
												// we can do this in children classes.

	public abstract String getString();

	public abstract boolean isCompatible(Card c);
}

class NumberCard extends Card {
	private static final long serialVersionUID = -2805011378357038501L;

	public NumberCard(String color, int value) {
		super(color, value);
	}

	public void hitPlayer(Player p) {
		// Numebr cards dont do anything special so just return
	}

	@Override
	public String getString() {
		return "Normal, " + color + ", " + value;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color) || c.getValue() == this.value;
	}
}

class AttackCard extends Card {
	private static final long serialVersionUID = -366474331822350548L;
	private int damage = 2;

	public AttackCard(String color, int damage) {
		super(color, 11);
		this.damage = damage;
	}

	public void hitPlayer(Player p) {
		Lobby.playerDraw(p, damage);
	}

	@Override
	public String getString() {
		return "Attack, " + color + ", " + damage;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color);
	}
}

class ReverseCard extends Card {
	private static final long serialVersionUID = 3324670375857667285L;

	public ReverseCard(String color) {
		super(color, 12);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		Lobby.offset = -Lobby.offset;
	}

	@Override
	public String getString() {
		return "Reverse, " + color;
	}

	@Override
	public boolean isCompatible(Card c) {	
		return c.color.equalsIgnoreCase(color);
	}
}

class SkipCard extends Card {
	private static final long serialVersionUID = 1L;

	public SkipCard(String color) {
		super(color, 13);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		Lobby.index += Lobby.offset;
	}

	@Override
	public String getString() {
		return "Skip, " + color;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color);
	}
}

class WildCard extends Card {
	private static final long serialVersionUID = 7660312161536462244L;

	public WildCard() {
		super(new String(""), 14);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		// TODO: prompt and get color
	}

	@Override
	public String getString() {
		return "Wild";
	}

	@Override
	public boolean isCompatible(Card c) {
		return true;
	}
}

class DrawFourCard extends Card {
	private static final long serialVersionUID = -9018622465925503256L;

	public DrawFourCard() {
		super(new String(""), 14);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		// TODO: prompt and get color
		Lobby.playerDraw(p, 4);
	}

	@Override
	public String getString() {
		return "+4";
	}

	@Override
	public boolean isCompatible(Card c) {
		return true;
	}
}

/* ===================================================== */
/*
 * Desription: As the name implies this is the deck of cards. Since this game
 * only requires one deck of cards I prefer using static methods in order to not
 * have multiple decks floating arround and get confused
/*====================================================== */
class Deck {
	// The main array list
	static public ArrayList<Card> cards = new ArrayList<Card>(); // Note: static variables must be defined, else you get
																	// a NULL error
	static public Card cardLast = new DrawFourCard();

	static public Card take(int i) {
		Card c = cards.get(i);
		cards.remove(i);
		return c;
	}

	static public void debugPrint() {
		System.out.println("Remaining cards: " + cards.size());
		for (Card c : cards)
			System.out.println(c + "," + c.getValue());
	}

	static public int size() {
		return cards.size();
	}

	static public void reset() {
		// Setup the cards
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				for (int i = 0; i < 10; i++) // Simply add all of the cards
					cards.add(new NumberCard(String, i + 1));

		// Add the attack cards
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				cards.add(new AttackCard(String, 2));
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				cards.add(new SkipCard(String));
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				cards.add(new ReverseCard(String));
		for (int j = 0; j < 4; j++)
			cards.add(new WildCard());
		for (int j = 0; j < 4; j++)
			cards.add(new DrawFourCard());
	}

}

/* ===================================================== */
/*
 * Desription: Basically holds the players and game data.
 * 
/*====================================================== */
class Player implements Serializable{
	private static final long serialVersionUID = 5241496991563179420L;
	public ArrayList<Card> hand = new ArrayList<Card>();

	public void printHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println("[" + (i + 1) + "] = " + hand.get(i).getString());
		}
	}
} 

//The lobby class is what holds the players, deck, deck offset, etc
class Lobby {
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
		SocketServer.SocketServerInterface ssi = new SocketServer.SocketServerInterface() 
		{
			@Override
			public void main(Socket s, Object o) {
				try {
					sServer.send(o);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}

			@Override
			public void connect(SocketServer.StructClient s) {
				try {																						//Attempt to send all the players the new player instance

					for(Player p : players)
					{
						s.send(p);
					}
					
					Player p = new Player();																//Add the player to the list
					playerReset(p);																			//Reset the players hand
					
					
					sServer.send(p);
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
			int index = 0;
			public void main(Object o) 
			{
				if(o.getClass() == String.class)
					System.out.println();
				else if(o.getClass() == Player.class)
				{
					players.add((Player) o);
					System.out.println("New player joined! Total players: " + players.size());
				}
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

class Game {
	public static Scanner scanner = new Scanner(System.in); 												// Create a Scanner object
	
	public static boolean Initalize()
	{
		System.out.println("Do you want to JOIN or CREATE a lobby?");										//Prompt user if needed to create a new lobby

		Lobby.reset();
				
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.print("] ");
			String newLine = scanner.nextLine();
			if(newLine.toLowerCase().equalsIgnoreCase("join"))												//If the user says join
			{
				System.out.print("What is the server ip: ");
				try {
					Lobby.joinLobby(scanner.nextLine(), SocketServer._port);
					break;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("ERROR: Failed to connect to server!");
				}
			}
			else if(newLine.toLowerCase().equalsIgnoreCase("create"))										//If the user says create
			{
				System.out.println(1);
				try { 
					Lobby.createLobby();
					Lobby.joinLobby("127.0.0.1", SocketServer._port);  
					Thread.sleep(100); 
					break;
					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace(); 
				}
				
			}
			else
			{
				System.err.println("ERROR: You need to JOIN or CREATE a server!");
			}
		}
		
		System.out.println("Initalized Lobby!");
		
		return true;
	}
	
	public static boolean Input() { 
		System.out.print("] ");
		String input = scanner.nextLine(); 
		
		try {
			Lobby.sClient.sendAndWait(input);
		} catch (IOException | InterruptedException e1) {
			return false;
		} 

		return true;
	}

	public static boolean Update() {
		if (!Input())
			return false;

		return true;
	}
}

/* ===================================================== */
/*
 * Main code and such 
/*=====================================================  */
public class Uno { 
	
	public static void main(String[] args) {

		if(!Game.Initalize())																	//Try and create the gae
		{
			System.err.println("ERROR: Something failed while trying to initalize!");			//A fatal error has occured
			System.exit(-1);																	//Kill java
		}
			
		
		while (Game.Update()) 																	//Try and make the game update
		{

		}
	}
}