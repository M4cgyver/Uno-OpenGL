package lwjgl3;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import resources.ResourceManager;

public abstract class Shader
{
	private int Id;
	private int vertexId;
	private int fragmentId;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	Shader(String vertexFile, String fragmentFile)
	{
		Id 			= GL20.glCreateProgram();
		vertexId 	= loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentId 	= loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		GL20.glAttachShader(Id, vertexId);
		GL20.glAttachShader(Id, fragmentId);
		bindAttributes();
		GL20.glLinkProgram(Id);
		GL20.glValidateProgram(Id);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String name)
	{
		return GL20.glGetUniformLocation(Id, name);
	}
	
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadBoolean(int location, boolean value)
	{
		float toLoad = 0;
		if(value) 	toLoad = 1;
		 
		GL20.glUniform1f(location, toLoad);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	public void start()
	{ 
		GL20.glUseProgram(Id);
	}
	
	public void stop()
	{
		//0 is the default shader (none)
		GL20.glUseProgram(0);
	}
	
	public void cleanup()
	{
		stop();
		GL20.glDetachShader(Id, vertexId);
		GL20.glDetachShader(Id, fragmentId);
		GL20.glDeleteShader(vertexId);
		GL20.glDeleteShader(fragmentId);
		GL20.glDeleteShader(Id);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int att, String variableName)
	{
		GL20.glBindAttribLocation(Id, att, variableName);
	}
	
	private static int loadShader(String file, int type)
	{ 
		int shaderID = GL20.glCreateShader(type);
		try {
			GL20.glShaderSource(shaderID, ResourceManager.getString(file));
			GL20.glCompileShader(shaderID);
			if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE)
			{
				System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
				System.err.println("Could not compile shader!");
				System.exit(-1);
			}
			return shaderID;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
}