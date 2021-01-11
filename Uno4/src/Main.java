public class Main {
	
	public static void main(String[] args) {
		
		Gui.Initalize();
		
		while(Gui.Update()) 
		{
			try {
				Thread.sleep(1000/144);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
	}
}