package lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class GLWindow {
	private int width, height;
	private String title;
	private long windowId; 
	
	public GLWindow(int width, int height, String title) 
	{
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public boolean create()
	{
		windowId = GLFW.glfwCreateWindow(width, height, title, 0, 0);									//Attempt to create the window on the primary monitor
		
		if(windowId == 0)																				//Check if we failed
		{
			System.err.println("ERROR: Lwjgl3 failed to create window!");								//If we did print that we failed
			return false;
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());					//Get the videomode
		GLFW.glfwSetWindowPos(windowId, (videoMode.width()-width)/2, (videoMode.height()-height)/2);	//Center the window
		GLFW.glfwShowWindow(windowId);																	//Show the window

		GLFW.glfwMakeContextCurrent(windowId); 
		GL.createCapabilities();
		GL11.glViewport(0, 0, width, height);
		
		return true;
	}
	
	public boolean update()
	{
		GLFW.glfwSwapBuffers(windowId);
		return !GLFW.glfwWindowShouldClose(windowId);
	}
}
