package lwjgl3;

//The raw modle holds data for the VAO and the VBO
//The Vao is a list of VBOs that contain data for opengl (like a std::vector<std::vector>>)
//The VBO is a list of data (floats, binaries, etc) for opengl (like a std::vector<T>

public class ModleRaw 
{
	private int vaoID;
	private int vertexCount;
	
	public ModleRaw(int vaoID, int vertexCount)
	{
		this.vertexCount 	= vertexCount;
		this.vaoID 			= vaoID;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
