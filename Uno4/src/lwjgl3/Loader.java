package lwjgl3;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import resources.ResourceManager;

//this loads the 3d modle into memory by storing positional data in a vao

public class Loader 
{
	private ArrayList<Integer>	vaos = new ArrayList<Integer>();		//Store all of the used VAOs so far;
	private ArrayList<Integer>	vbos = new ArrayList<Integer>();		//Store all of the used VBOs so far;
	private ArrayList<Integer>	textures = new ArrayList<Integer>();	//Store all of the used textures so far;
	
	public ModleRaw loadToVAO(float[] position, float[] textureCoords, int[] indecies)
	{
		int vaoID = createVAO();
		bindIndeciesBuffer(indecies);
		storeDataInAttributeList(0,3,position);
		storeDataInAttributeList(1,2,textureCoords);
		unbindVAO();
		return new ModleRaw(vaoID, indecies.length);
	}
	
	public int	loadTexture(String file)
	{
		Texture t = null;
		try 
		{
			t = TextureLoader.getTexture("PNG", ResourceManager.getStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int textureId = t.getTextureID();
		textures.add(textureId);
		return textureId;
				
	}
	
	public void cleanup()
	{
		for(int vao:vaos)	GL30.glDeleteVertexArrays(vao);
		for(int vbo:vbos)	GL15.glDeleteBuffers(vbo);
		for(int t:textures)	GL11.glDeleteTextures(t);
	}
	
	private int createVAO() 
	{
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordsize, float[] data)
	{
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordsize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndeciesBuffer(int[] indecies)
	{
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = storeDataInIntBuffer(indecies);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
