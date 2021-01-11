import java.io.Serializable;

import org.lwjglx.util.vector.Vector3f;

/*=====================================================*/
/* Desription: This is the card class, this just holds
 * data in order to figure out what card is either in
 * the deck or is in the players hand, only holds basic
 * values and the getters/setters
/*=====================================================*/
public abstract class Card implements Serializable{
	private static final long serialVersionUID = 5051683348763987382L;
	public static String[] Strings = { "red", "blue", "green", "yellow" }; // Incase for whatever reason we wanted to add more
	public static Vector3f[] Colors =  { new Vector3f(1,0.25f,0.25f), new Vector3f(0.25f,0.25f,1), new Vector3f(0.25f,1,0.25f), new Vector3f(1,1,0.25f) };
	protected String color; // Note: This is a color object. Java has predefined libraries for colors, so we
							// can use them to make custom collors if needed
	protected int value;

	// Basic getters and setters
	public Card(String color, int value) {
		this.color = color;
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setString(String color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// Check if card is a match
	public boolean isMatch(Card c) {
		return color.equals(c.color) && value == c.getValue();
	}

	// Abstract functions for later use
	public abstract void hitPlayer(Player p); // Sometimes we have a special card (+4, +2, etc) and we want to do
												// something special to the player
												// we can do this in children classes.

	public static Vector3f getColor(String color)
	{
		for(int i = 0; i < Strings.length; i++)
			if(color.equalsIgnoreCase(Strings[i])) return Colors[i];
		
		return new Vector3f();
	}
	
	public abstract String getString();

	public abstract boolean isCompatible(Card c);
}

class NullCard extends Card
{ 
	private static final long serialVersionUID = 2491331752481050804L;

	public NullCard() {
		super("", 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void hitPlayer(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompatible(Card c) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

class NumberCard extends Card {
	private static final long serialVersionUID = -2805011378357038501L;

	public NumberCard(String color, int value) {
		super(color, value);
	}

	public void hitPlayer(Player p) {
		// Numebr cards dont do anything special so just return
	}

	@Override
	public String getString() {
		return "Normal, " + color + ", " + value;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color) || c.getValue() == this.value;
	}
}

class AttackCard extends Card {
	private static final long serialVersionUID = -366474331822350548L;
	private int damage = 2;

	public AttackCard(String color, int damage) {
		super(color, 11);
		this.damage = damage;
	}

	public void hitPlayer(Player p) {
		Lobby.playerDraw(p, damage);
	}

	@Override
	public String getString() {
		return "Attack, " + color + ", " + damage;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color);
	}
}

class ReverseCard extends Card {
	private static final long serialVersionUID = 3324670375857667285L;

	public ReverseCard(String color) {
		super(color, 12);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		Lobby.offset = -Lobby.offset;
	}

	@Override
	public String getString() {
		return "Reverse, " + color;
	}

	@Override
	public boolean isCompatible(Card c) {	
		return c.color.equalsIgnoreCase(color);
	}
}

class SkipCard extends Card {
	private static final long serialVersionUID = 1L;

	public SkipCard(String color) {
		super(color, 13);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		Lobby.index += Lobby.offset;
	}

	@Override
	public String getString() {
		return "Skip, " + color;
	}

	@Override
	public boolean isCompatible(Card c) {
		return c.color.equalsIgnoreCase(color);
	}
}

class WildCard extends Card {
	private static final long serialVersionUID = 7660312161536462244L;

	public WildCard() {
		super(new String(""), 14);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		// TODO: prompt and get color
	}

	@Override
	public String getString() {
		return "Wild";
	}

	@Override
	public boolean isCompatible(Card c) {
		return true;
	}
}

class DrawFourCard extends Card {
	private static final long serialVersionUID = -9018622465925503256L;

	public DrawFourCard() {
		super(new String(""), 14);
	}

	public void hitPlayer(Player p) {
		// Now we just need to reverse the card
		// TODO: prompt and get color
		Lobby.playerDraw(p, 4);
	}

	@Override
	public String getString() {
		return "+4";
	}

	@Override
	public boolean isCompatible(Card c) {
		return true;
	}
}