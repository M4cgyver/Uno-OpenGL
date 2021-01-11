import java.io.IOException;
import java.util.Scanner;

import Internet.SocketServer;

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
		String input = scanner.nextLine(); 
		
		try {
			Lobby.sClient.send(input);
			Thread.sleep(100);
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
