package lwjgl3;

public class ShaderBasic extends Shader{

	public ShaderBasic() 
	{
		super("/shaders/vertexShader.txt", "/shaders/fragmentShader.txt");	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		
	}
}
