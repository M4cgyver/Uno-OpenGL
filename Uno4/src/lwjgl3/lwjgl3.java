package lwjgl3;

import org.lwjgl.glfw.GLFW;

public class lwjgl3 {
	
	public static boolean Initalize()
	{
		if(!GLFW.glfwInit())											//Attempt to initalize GLFW
		{
			System.err.println("ERROR: glfw couldnt initalize!");		//Print initaliz failed
			return false;
		}
		
		return true;
	}
	
	public static void Poll()
	{
		GLFW.glfwPollEvents();
	}
}
