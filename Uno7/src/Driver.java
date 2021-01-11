/*====================================================================================================================================/*
 * Created BY:	Logan Rios
 * Date: 		12/19/2020
 * I used references like ThinMatrix and CodingAp in order to learn and understand the LWJGL library
 * I used multiple tutorials and references (mainly StackOverflow) in order to create a wrapper for the socket and such
 * Used the uno game manual in order to program the main game logic*/
/*====================================================================================================================================*/

public class Driver {
	
	public static void main(String[] args) {

		Gui.Initalize();
		Gui.Run();
		
		while(Gui.bInitalized!=true)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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