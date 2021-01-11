import java.io.IOException;
import java.util.ArrayList;

import org.lwjglx.util.vector.Vector3f;

import lwjgl3.CameraPov;
import lwjgl3.Entity;
import lwjgl3.GLTexture;
import lwjgl3.GLWindow;
import lwjgl3.Loader;
import lwjgl3.ModleRaw;
import lwjgl3.ModleTexture;
import lwjgl3.ObjectLoader;
import lwjgl3.Render;
import lwjgl3.ShaderBasic;

public class Gui {
	static GLWindow 	window;		//All of the gui and opengl variables
	static Render		renderer;
	static Loader		loader;
	static CameraPov	camera;

	static ArrayList<ModleTexture>	eCards;			//Entity of all the cards
	static Entity					eTable;			//Entity of the table	
	static Entity					eFloor;			//Entity of the floor	
	static ShaderBasic				shBasic;		//The basic shader for opengl
	static ShaderCard				shCard;			//The shader in order to recolor the cards
	static Entity 					eZero;			//The zero point 
	static boolean					bInitalized;
	
	static Thread	tMain;			//The thread in order to update the main window 
	static float	fElapsed = 0;	//The total elapsed time
	
	public static void Initalize()
	{
		bInitalized = false;
		
		Runnable rMain = () ->								//Create a runnable class to get input
			{
				lwjgl3.lwjgl3.Initalize();						//Attempt to initalize the main lwjgl3 libray 
				window = new GLWindow(800,600, "test");			//Create the window class
				window.create();								//Create the window
				
				loader 		= new Loader();										//Make a new Loader instance
				shBasic 	= new ShaderBasic();								//Make a new basic shader instance
				shCard		= new ShaderCard();									//Make a new instance of the shader card
				renderer 	= new Render(800, 600);								//Make a new Renderer instance
				eZero		= new Entity(null, new Vector3f(), 0, 180, 0, 0);	//Make a new entity at zero 
				camera		= new CameraPov(eZero);		  
				
				renderer.loadProjectionMatrixOntoShader(shBasic);				//Load the projection matrix onto the basic shader
				renderer.loadProjectionMatrixOntoShader(shCard);				//Load the projection matrix onto the card shader
				
				ModleRaw mCard, mTable, mFloor;
				try {
					mCard 	= ObjectLoader.loadObjModleFromJar("/objects/unocard.obj", loader);
					mTable 	= ObjectLoader.loadObjModleFromJar("/objects/table.obj", loader);
					mFloor 	= ObjectLoader.loadObjModleFromJar("/objects/floor.obj", loader);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				
				ModleTexture tTable = new ModleTexture(mTable, new GLTexture(loader.loadTexture("/objects/table.png")));
				eTable = new Entity(tTable, new Vector3f(0,-5,-15),0,0,0,3);

				ModleTexture tFloor = new ModleTexture(mFloor, new GLTexture(loader.loadTexture("/objects/floor.png")));
				eFloor = new Entity(tFloor, new Vector3f(0,14,-12*(10f/3f)),0,0,0,10);
				
				eCards		= new ArrayList<ModleTexture>();	//Next we need to load all of the textures into the array
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard.png"))));		//[0] Uno back card
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard1.png"))));	//[1] Uno face card 1
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard2.png"))));	//[2] Uno face card 2
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard3.png"))));	//[3] Uno face card 3
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard4.png"))));	//[4] Uno face card 4
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard5.png"))));	//[5] Uno face card 5
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard6.png"))));	//[6] Uno face card 6
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard7.png"))));	//[7] Uno face card 7
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard8.png"))));	//[8] Uno face card 8
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard9.png"))));	//[9] Uno face card 9
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocardr.png"))));	//[10] Uno face card Reverse
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard+2.png"))));	//[11] Uno face card +2
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocardw.png"))));	//[12] Uno face card Wild
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocard+4.png"))));	//[13] Uno face card Wild +4
				eCards.add(new ModleTexture(mCard, new GLTexture(loader.loadTexture("/objects/unocards.png"))));	//[14] Uno face skip
				
				bInitalized = true;
				
				while(Gui.Update()) 							//Run the window
				{
					try {
						Thread.sleep(1000/60);					//Sleep and run @ 144
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
				
				System.exit(0);									//Exit the main system thread
			};
			
			tMain = new Thread(rMain);					//Create the main thread
	}
	 
	public static void Run()
	{
		tMain.start();									//Start the main thread
	}
	
	public static boolean Update()
	{
		lwjgl3.lwjgl3.Poll();							//Poll through all of the events
 
		camera.move();									//Update the camera 
		 
		renderer.prepare(); 							//Prepare the renderer 
		shBasic.start();								//Start the basic shader
		shBasic.loadViewMatrix(camera);					//Load the projection matrix
		renderer.render(eTable, shBasic);				//Render the table
		renderer.render(eFloor, shBasic);				//Render the floor
		shBasic.stop();  								//Stop the shader
		
		ArrayList<Card> cardsToDraw = new ArrayList<Card>();
		if(Lobby.phCurrent == Lobby.PHASE.GAME)										//Check if we are in game
		{
			cardsToDraw = Lobby.pdata.players.get(Lobby.pdata.currentPlayer).hand;	//Get the current card
		}
		else
		{ 
			for(int i = 0; i < 7; i++) cardsToDraw.add(new NullCard());
			fElapsed += .25f;
			eZero.setRy(fElapsed);
		}
		
		int handSize = cardsToDraw.size(); 
		for(int i = 0; i < handSize; i++)
		{
			float xoffset = ((float)i/(float)handSize)*4*1.5f;											//Get the xoffset of the card
			xoffset -= 1.5f*1.5f;
			Vector3f position = new Vector3f(xoffset,-4.25f,-6f+((float)i/100f));					//Get the current position of the vector
			Card currentCard = cardsToDraw.get(i);													//Get the current cards
			
			Vector3f secondaryColor = Card.getColor(currentCard.color);								//Gets the secondary collor fromt he card class
			
			if(currentCard.getClass() == NumberCard.class)											//The Number card
			{
				Entity e = new Entity(eCards.get(currentCard.value), position,-45/2/2,90,0,1);			//Get the current entity with the texture
				shCard.start();																			//Start the shader
				shCard.loadViewMatrix(camera);															//Load the projection matrix
				shCard.loadSecondaryColor(secondaryColor);												//Load the secondary color
				renderer.render(e, shCard);																//Render the object given the card shader
				shCard.stop();																			//Stop the shader
			}
			else if(currentCard.getClass() == ReverseCard.class)									//The reverse card
			{
				Entity e = new Entity(eCards.get(10), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shCard.start();																			//Start the shader
				shCard.loadViewMatrix(camera);															//Load the projection matrix
				shCard.loadSecondaryColor(secondaryColor);												//Load the secondary color
				renderer.render(e, shCard);																//Render the object given the card shader
				shCard.stop();																			//Stop the shader
			}
			else if(currentCard.getClass() == AttackCard.class)										//The attack card
			{
				Entity e = new Entity(eCards.get(11), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shCard.start();																			//Start the shader
				shCard.loadViewMatrix(camera);															//Load the projection matrix
				shCard.loadSecondaryColor(secondaryColor);												//Load the secondary color
				renderer.render(e, shCard);																//Render the object given the card shader
				shCard.stop();																			//Stop the shader
			}
			else if(currentCard.getClass() == WildCard.class)										//The wild card
			{
				Entity e = new Entity(eCards.get(12), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shBasic.start();																		//Start the shader
				renderer.render(e, shBasic);															//Render the object given the basic shader
				shBasic.stop();																			//Stop the shader
			}
			else if(currentCard.getClass() == DrawFourCard.class)									//The +4 card
			{
				Entity e = new Entity(eCards.get(13), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shBasic.start();																		//Start the shader
				renderer.render(e, shBasic);															//Render the object given the basic shader
				shBasic.stop();																			//Stop the shader
			}
			else if(currentCard.getClass() == SkipCard.class)										//The skip card
			{
				Entity e = new Entity(eCards.get(14), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shCard.start();																			//Start the shader
				shCard.loadViewMatrix(camera);															//Load the projection matrix
				shCard.loadSecondaryColor(secondaryColor);												//Load the secondary color
				renderer.render(e, shBasic);															//Render the object given the card shader
				shCard.stop();	
			}
			else																					//There wasnt a card found?
			{
				Entity e = new Entity(eCards.get(0), position,-45/2/2,90,0,1);							//Get the current entity with the texture
				shBasic.start();																		//Start the shader
				renderer.render(e, shBasic);															//Render the object given the basic shader
				shBasic.stop();																			//Stop the shader
			}
		}

		
		return window.update();							//Return if the window needs to close
	}
}
