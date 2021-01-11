package lwjgl3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

import resources.ResourceManager;

//This is a static class in order to load objects from file/res.
//If it isnt objvious
public class ObjectLoader 
{
	private static void processVertex(	String[] vertexData, 
										List<Integer> indicies, List<Vector2f> textures, List<Vector3f> normals, 
										float[] textureArray, float[] normalsArray)
	{
		int currentVertexPointer 				= Integer.parseInt(vertexData[0]) -1;
		indicies.add(currentVertexPointer);
		Vector2f currentTex 					= textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2]	= currentTex.x;
		textureArray[currentVertexPointer*2+1]	= 1-currentTex.y;
		Vector3f currentNorm 					= normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3]	= currentNorm.x;
		normalsArray[currentVertexPointer*3+1]	= currentNorm.y;
		normalsArray[currentVertexPointer*3+2]	= currentNorm.z;
	
	}
	
	public static ModleRaw loadObjModleFromFile(String filename, Loader loader) throws IOException
	{
		FileReader fr 			= new FileReader(new File("res/" + filename + ".obj"));
		BufferedReader reader 	= new BufferedReader(fr);
		
		String line;
		List<Vector3f> vertecies 	= new ArrayList<Vector3f>();
		List<Vector2f> textures 	= new ArrayList<Vector2f>();
		List<Vector3f> normals 		= new ArrayList<Vector3f>();
		List<Integer> indices 		= new ArrayList<Integer>();
		
		Boolean	barrays			= false;
		float[]	verteciesArray 	= null;
		float[]	texturesArray	= null;
		float[]	normalsArray	= null;
		int[]	indiciesArray	= null;
		
		while(true)
		{
			line = reader.readLine();
			if(line==null)break;				//end of file
			String[] tokens = line.split(" ");
			
			switch(tokens[0])
			{
			case "vn":		//normal coords
				Vector3f normal = new Vector3f(	Float.parseFloat(tokens[1]),
												Float.parseFloat(tokens[2]),
												Float.parseFloat(tokens[3]));
				normals.add(normal);
				break;
			case "vt":		//texture coords
				Vector2f texture = new Vector2f(	Float.parseFloat(tokens[1]),
													Float.parseFloat(tokens[2]));
				textures.add(texture);
				
				break;
			case "v":		//vert coord
				Vector3f vertex = new Vector3f(	Float.parseFloat(tokens[1]),
												Float.parseFloat(tokens[2]),
												Float.parseFloat(tokens[3]));
				vertecies.add(vertex);
				
				break;
			case "f":		//face coords
				//F is normally at the end of the file, after all of the arrays are init, so lets go ahead and load the arrays
				if(!barrays)
				{
					texturesArray	= new float[vertecies.size()*2];
					normalsArray 	= new float[vertecies.size()*3];
					barrays = true;
				}
				
				String[] vertex1 = tokens[1].split("/");
				String[] vertex2 = tokens[2].split("/");
				String[] vertex3 = tokens[3].split("/");
	
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
			
				break;
			}		
		}
		
		reader.close();
		
		verteciesArray		= new float[vertecies.size() * 3];
		indiciesArray 		= new int[indices.size()];
		
		int vertexPointer	= 0;
		for (Vector3f vertex:vertecies)
		{
			verteciesArray[vertexPointer++] = vertex.x;
			verteciesArray[vertexPointer++] = vertex.y;
			verteciesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indiciesArray[i]	= indices.get(i);
		}
		
		return loader.loadToVAO(verteciesArray, texturesArray, indiciesArray);
	}
	
	public static ModleRaw loadObjModleFromJar(String filename, Loader loader) throws IOException
	{
		BufferedReader reader 	=  new BufferedReader(new InputStreamReader(ResourceManager.getStream(filename), "UTF-8"));
		String line;
		List<Vector3f> vertecies 	= new ArrayList<Vector3f>();
		List<Vector2f> textures 	= new ArrayList<Vector2f>();
		List<Vector3f> normals 		= new ArrayList<Vector3f>();
		List<Integer> indices 		= new ArrayList<Integer>();
		
		Boolean	barrays			= false;
		float[]	verteciesArray 	= null;
		float[]	texturesArray	= null;
		float[]	normalsArray	= null;
		int[]	indiciesArray	= null;
		
		while(true)
		{
			line = reader.readLine();
			if(line==null)break;				//end of file
			String[] tokens = line.split(" ");
			
			switch(tokens[0])
			{
			case "vn":		//normal coords
				Vector3f normal = new Vector3f(	Float.parseFloat(tokens[1]),
												Float.parseFloat(tokens[2]),
												Float.parseFloat(tokens[3]));
				normals.add(normal);
				break;
			case "vt":		//texture coords
				Vector2f texture = new Vector2f(	Float.parseFloat(tokens[1]),
													Float.parseFloat(tokens[2]));
				textures.add(texture);
				
				break;
			case "v":		//vert coord
				Vector3f vertex = new Vector3f(	Float.parseFloat(tokens[1]),
												Float.parseFloat(tokens[2]),
												Float.parseFloat(tokens[3]));
				vertecies.add(vertex);
				
				break;
			case "f":		//face coords
				//F is normally at the end of the file, after all of the arrays are init, so lets go ahead and load the arrays
				if(!barrays)
				{
					texturesArray	= new float[vertecies.size()*2];
					normalsArray 	= new float[vertecies.size()*3];
					barrays = true;
				}
				
				String[] vertex1 = tokens[1].split("/");
				String[] vertex2 = tokens[2].split("/");
				String[] vertex3 = tokens[3].split("/");
	
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				
				break;
			}		
		}
		
		reader.close();
		
		verteciesArray		= new float[vertecies.size() * 3];
		indiciesArray 		= new int[indices.size()];
		
		int vertexPointer	= 0;
		for (Vector3f vertex:vertecies)
		{
			verteciesArray[vertexPointer++] = vertex.x;
			verteciesArray[vertexPointer++] = vertex.y;
			verteciesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indiciesArray[i]	= indices.get(i);
		}
		
		return loader.loadToVAO(verteciesArray, texturesArray, indiciesArray);
	}
}
