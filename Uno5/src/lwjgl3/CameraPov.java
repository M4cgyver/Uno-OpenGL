package lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjglx.input.Mouse;

public class CameraPov extends Camera{
	private float distanceFromPlayer = 5;
	private float angleAroundPlayer = 0;
	private float sensitivity = 1.0f;
	public Entity player;
	
	public CameraPov(Entity player)
	{
		this.player = player;
	}

	public void move()
	{  
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float fHorizontalDistance = calculateHorizontalDistance();
		float fVerticalDistance = calculateVerticalDistance();
		calculateCameraPosition(fHorizontalDistance, fVerticalDistance); 
		this.yaw = 180 - (player.getRy() + angleAroundPlayer);
	}
	
	public void calculateCameraPosition(float horizDistance, float verticDistance)
	{
		float fTheta = player.getRy() + angleAroundPlayer;
		float fOffX = horizDistance * (float)Math.sin(Math.toRadians(fTheta));
		float fOffZ = horizDistance * (float)Math.cos(Math.toRadians(fTheta));
		position.x = player.getPosition().x-fOffX;
		position.z = player.getPosition().z-fOffZ;
		position.y = player.getPosition().y + verticDistance;
	}
	
	public float calculateHorizontalDistance()
	{
		return distanceFromPlayer * (float)Math.cos(Math.toRadians(pitch));
	}

	public float calculateVerticalDistance()
	{
		return distanceFromPlayer * (float)Math.sin(Math.toRadians(pitch));
	}
	
	public void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * sensitivity * 0.03f;
		distanceFromPlayer -= zoomLevel;
	}
	
	public void calculatePitch()
	{
		if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
		{
			float fPitchChange =  (float) (Input.getMouseDY()*sensitivity/2);
			pitch -= fPitchChange;
		}
	}
	
	public void calculateAngleAroundPlayer()
	{
		if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) 
		{
			float fAngleChange = (float) (Input.getMouseDX()*sensitivity/2);
			angleAroundPlayer -= fAngleChange;
		}
	}
}
