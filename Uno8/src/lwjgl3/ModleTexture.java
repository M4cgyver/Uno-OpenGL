package lwjgl3;

public class ModleTexture
{
	private GLTexture 	texture;
	private ModleRaw	rawModle;
	
	public ModleTexture(ModleRaw modle, GLTexture t)
	{
		this.rawModle	= modle;
		this.texture 	= t;
	}

	public ModleRaw getRawModle() {
		return rawModle;
	}

	public GLTexture getTexture() {
		return texture;
	}
}
