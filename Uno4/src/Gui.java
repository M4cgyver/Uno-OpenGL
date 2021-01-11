import lwjgl3.GLTexture;
import lwjgl3.GLWindow;
import lwjgl3.Loader;
import lwjgl3.ModleRaw;
import lwjgl3.ModleTexture;
import lwjgl3.Render;
import lwjgl3.ShaderBasic;

public class Gui {
	static GLWindow 	window;
	static ShaderBasic 	shBasic;
	static Render		renderer;
	static Loader		loader;
	static ModleTexture	tmodle;
	
	public static void Initalize()
	{
		lwjgl3.lwjgl3.Initalize();						//Attempt to initalize the main lwjgl3 libray 
		window = new GLWindow(800,600, "test");			//Create the window class
		window.create();								//Create the window
		
		loader 		= new Loader();						//Make a new Loader instance
		renderer 	= new Render();						//Make a new Renderer instance
		shBasic 	= new ShaderBasic();				//Make a new basic shader instance
		

		float[] vertices = 
			{
				-0.5f, 0.5f, 0.0f,		//V0
				-0.5f,-0.5f, 0.0f,		//V1
				 0.5f,-0.5f, 0.0f,		//V2
				 0.5f, 0.5f, 0.0f,		//V3
			};
		
		int[] indecies = 
			{
				0,1,3,					//Top left triangle
				3,1,2,					//Bottom right triangle
			};
		
		float[] textCoords = 
			{
				0,0,					//V0
				0,1,					//V1
				1,1,					//V2
				1,0,					//V3
			};

		ModleRaw model 			= loader.loadToVAO(vertices, textCoords, indecies);
		GLTexture texture		= new GLTexture(loader.loadTexture("/test.png"));
		tmodle					= new ModleTexture(model, texture);
		
	}
	
	public static boolean Update()
	{
		lwjgl3.lwjgl3.Poll();
		
		renderer.prepare();
		shBasic.start();
		renderer.render(tmodle);
		shBasic.stop();
		
		return window.update();
	}
}
