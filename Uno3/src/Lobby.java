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
	public static ArrayList<Player>	players 	= new ArrayList<Player>();
	public static String			color;
	public static int 				index		= 0;
	public static int				offset		= 1; 
	public static PHASE 			phCurrent	= PHASE.LOBBY;
	
	//Internet sockets
	public static SocketServer 		sServer		= null;
	public static SocketClient 		sClient		= null;
	
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
			public void recieve(Socket s, Object o) {
				try {	
					int sIndex = sServer.getSocketIndex(s);													//Save the socket index, as it pertains
					StructClient client = sServer.getClients().get(sIndex);									//Save the current client
					String input = (String)o;
					String[] tokens = input.split(" ");
					
					sServer.send("Player " + (sIndex+1) + ": " + input);
					
					if(phCurrent.equals(PHASE.LOBBY))														//if we are still in the lobby
					{
						sServer.send(new String("Player " + (sIndex+1) + ": " + input));
						
						if(input.equals("ready") && sIndex == 0)
						{
							sServer.send(new String("Prepairing to start the game!"));
							sServer.send(new String("Resetting the deck..."));
							Deck.reset(); 
							sServer.send(new String("Sending players data...")); 
							
							for(int i = 0; i < 5; i++)
							{
								sServer.send("Starting in " + (5-i) + "...");
								Thread.sleep(1000);
							}
							
							sServer.send(new String("==============================================================="));
							for(SocketServer.StructClient c : sServer.getClients()) 
							{
								c.send(new String("You are player " + (sServer.getSocketIndex(client.socket)+1) + "!"));
								c.send(new String("Type /help to get all of your commands!"));
								c.send(new String("Try and get to 0 cards in order to win!"));
								c.send(new String("==============================================================="));
							}
							
							Lobby.phCurrent = Lobby.PHASE.GAME;
						}
					}
					
					if(phCurrent.equals(PHASE.GAME))
					{
						switch(tokens[0])
						{
						case "/deck":
							String playerString = players.get(sIndex).toString() + "\n[L] = " + Deck.data.cardLast.getString();
							sServer.getClients().get(sIndex).send(new String("Current Hand: \n" + playerString));
							break;
							
						case "/draw":
							int cardsToTake = 0;
		
							try
							{
								cardsToTake = Integer.parseInt(tokens[1]);
							}catch (Exception e) 
							{
								sServer.getClients().get(sIndex).send(new String("Error!"));
								break;
							}
							
							for(int i = 0; i < cardsToTake; i++)
							{
								Card c = Deck.take();
								sServer.getClients().get(sIndex).send(new String("New card aquired! " + c.getString()));
								players.get(sIndex).hand.add(c);
							}
							
							break;
						
						case "/players":
							String sReturn = "";
							
							for(int i = 0; i < players.size()-1; i++)
								sReturn += "Player " + (i+1) + " has " + players.get(i).hand.size() + " cards! \n";
							sReturn += "Player " + (players.size()) + " has " + players.get(players.size()-1).hand.size() + " cards!";

							sServer.getClients().get(sIndex).send(new String(sReturn));
							sServer.getClients().get(sIndex).send(new String("Its Player " + (Lobby.getPlayerTurn() + 1) + " turn!"));
							
							break;
						
						case "/attack":
							if(Lobby.getPlayerTurn() != sIndex)																						//If the player index isnt the same as the turn
							{
								client.send(new String("Error: Its not your turn yet!"));															//Return saying it aint ur turn
								break;																												//breakk
							}
							
							int inSize = 0;
							try																														//Attempt to parse the tokens[1] 
							{
								inSize = Integer.parseInt(tokens[1]);																				//Convert
							} catch(Exception e) 																									//If you catch the exception
							{
								client.send("Error: couldnt parse the # you gave!");																//Send the error
								break;
							}
							
							Player playerCurrent = Lobby.players.get((int) Lobby.getPlayerTurn());
							Player playerNext = Lobby.players.get((int) Lobby.getPlayerTurn(Lobby.index + Lobby.offset));

							int handSize = playerCurrent.hand.size(); 

							if (inSize > 0 && handSize >= inSize) {
								// Check if the cards in the deck are compatible
								Card currentCard = playerCurrent.hand.get(inSize - 1);

								if (Deck.data.cardLast == null || (Deck.data.cardLast.isCompatible(currentCard)
										|| currentCard.isCompatible(Deck.data.cardLast))) {
									// Print
									client.send(new String("Player [" + (Lobby.getPlayerTurn() + 1) + "] just attacked ["
											+ (Lobby.getPlayerTurn(Lobby.index + Lobby.offset) + 1) + "] placed a ["
											+ currentCard.getString() + "]!"));

									// Logic
									currentCard.hitPlayer(playerNext);
									Deck.data.cards.add(currentCard);
									playerCurrent.hand.remove(currentCard);
									Lobby.index += Lobby.offset;
									Deck.data.cardLast = currentCard;

								} else
									client.send(new String("ERR: Your card [" + currentCard.getString()
											+ "] is incompatible with the last played card [" + Deck.data.cardLast.toString() + "]!"));
							}

							else
								client.send(new String("ERR: That card index is out of bounds!"));
							
							break;
		
						case "/help":
							String sHelp = 	"List of current commands: \n" + 
											"/deck          Shows the current deck in your hand.\n" + 
											"/players       Checks out the players and their decks.\n" + 
											"/draw #        Draws # of cards from the deck.\n" + 
											"/attack #      Attacks the next player with the index\n" +
											"               of the card in your hand (given #).";

							client.send(new String(sHelp));
							
							break;
							
						default:
							break;
						}
						
						//Check for endgame win conditions
						for(Player p : players)																										//Goes through the player list as p
						{
							if(p.hand.size()<=0)																									//Check the player hand size
							{

								
								sServer.send(new String("\n==============================================================="));
								sServer.send(new String("We have a winner! Player " + (players.indexOf(p)+1) + " has reached 0 cards!"));
								sServer.send(new String("Everyone has returned to the main lobby.")); 
								sServer.send(new String("===============================================================\n"));
								
								phCurrent = PHASE.LOBBY;																							//Set the current game as the lobby
							}
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.exit(-1);
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
			@SuppressWarnings("unchecked")
			public void recieve(Object o) 
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

	public static boolean isHost() {return sServer!=null;}
}