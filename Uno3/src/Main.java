
public class Main {
	
	public static void main(String[] args) {

		if(!Game.Initalize())																	//Try and create the gae
		{
			System.err.println("ERROR: Something failed while trying to initalize!");			//A fatal error has occured
			System.exit(-1);																	//Kill java
		}

		System.out.println("\n\nAlrighty, feel free to type to the other users in the game! ");			
		
		while (Game.Update()) 																	//Try and make the game update
		{

		}
	}
}