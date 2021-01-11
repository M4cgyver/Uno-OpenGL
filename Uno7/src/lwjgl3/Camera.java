package lwjgl3;

import org.lwjglx.util.vector.Vector3f;

public class Camera {
	
	protected Vector3f position = new Vector3f();
	protected float pitch=0;
	protected float yaw=0;
	protected float roll=0;
	
	public Camera()
	{
		
	}
	
	public void move(float fVary)
	{
		
		
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	} 
}
