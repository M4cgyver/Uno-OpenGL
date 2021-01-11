package resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceManager 
{ 
	public static InputStream getStream(String filename) throws IOException
	{
		return ResourceManager.class.getResourceAsStream(filename);
	}
	
	public static String getString(String filename) throws IOException
	{ 
		InputStream inStream 		= ResourceManager.class.getResourceAsStream(filename);
		InputStreamReader inRead 	= new InputStreamReader(inStream);
		BufferedReader reader 		= new BufferedReader(inRead);
		String ret 					= "";
		String line 				= ""; 
		try {
			while((line = reader.readLine())!=null) ret += line + '\n';
		} catch (IOException e) {
			throw e;
		}
		
		return ret;
	}
}
