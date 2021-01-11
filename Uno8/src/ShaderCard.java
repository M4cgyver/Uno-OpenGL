

import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import lwjgl3.Camera;
import lwjgl3.Maths;
import lwjgl3.Shader;

public class ShaderCard extends Shader
{
	private int location_transformationMatrix; 
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_secondaryColor;
	
	public ShaderCard() 
	{
		super("/shaders/cardVertexShader.txt", "/shaders/cardFragmentShader.txt");	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_secondaryColor = super.getUniformLocation("newColor");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	

	public void loadSecondaryColor(Vector3f vector){
		super.loadVector(location_secondaryColor, vector);
	}

}