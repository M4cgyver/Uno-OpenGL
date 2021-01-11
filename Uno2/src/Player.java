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

	public void printHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println("[" + (i + 1) + "] = " + hand.get(i).getString());
		}
	}
} 