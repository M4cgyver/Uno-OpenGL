import java.io.Serializable;
import java.util.ArrayList;

/* ===================================================== */
/*
 * Desription: Basically holds the players and game data.
 * 
/*====================================================== */
class Player implements Serializable{
	private static final long serialVersionUID = 5241496991563179420L;
	public ArrayList<Card> hand = new ArrayList<Card>();

	public String toString() {
		String sRet = "";
		
		for (int i = 0; i < hand.size()-1; i++) 
			sRet += "[" + (i + 1) + "] = " + hand.get(i).getString() + "\n";
		sRet += "[" + (hand.size()) + "] = " + hand.get(hand.size()-1).getString();
		
		return sRet;
	}
} 