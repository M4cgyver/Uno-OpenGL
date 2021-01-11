import java.io.Serializable;
import java.util.ArrayList;

/* ===================================================== */
/*
 * Desription: As the name implies this is the deck of data.cards. Since this game
 * only requires one deck of data.cards I prefer using static methods in order to not
 * have multiple decks floating arround and get confused
/*====================================================== */

public class Deck {
	// The main array list
	static class Data implements Serializable
	{
		private static final long serialVersionUID = 1907450758697123567L;
		public ArrayList<Card> cards = new ArrayList<Card>(); // Note: static variables must be defined, else you get
		// a NULL error
		public Card cardLast = new DrawFourCard();

	}
	
	static Data data;
	
	static public Card take(int i) {
		Card c = data.cards.get(i);
		data.cards.remove(i);
		return c;
	}

	static public void debugPrint() {
		System.out.println("Remaining data.cards: " + data.cards.size());
		for (Card c : data.cards)
			System.out.println(c.getString());
	}

	static public int size() {
		return data.cards.size();
	}

	static public void reset() {
		data = new Deck.Data();
				
		// Setup the data.cards
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				for (int i = 0; i < 10; i++) // Simply add all of the data.cards
					data.cards.add(new NumberCard(String, i + 1));

		// Add the attack data.cards
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				data.cards.add(new AttackCard(String, 2));
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				data.cards.add(new SkipCard(String));
		for (int j = 0; j < 2; j++)
			for (String String : Card.Strings)
				data.cards.add(new ReverseCard(String));
		for (int j = 0; j < 4; j++)
			data.cards.add(new WildCard());
		for (int j = 0; j < 4; j++)
			data.cards.add(new DrawFourCard());
	}

}

