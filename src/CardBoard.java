import java.util.ArrayList;

/**
 * This class represents a Board that can be used in a collection
 * of solitaire games similar to Elevens.  The variants differ in
 * card removal and the board size.
 */
public abstract class CardBoard 
{

	private DeckOfCards deck; //the deck of cards being used to play current game
	private Card[] cards;

	public CardBoard(int size, String[] ranks, String[] suits, int[] pointValues) 
	{
		cards = new Card[size]; //size is the number of cards on the board
		deck = new DeckOfCards(true);
		dealMyCards();
	}

	public void newGame() 
	{
		deck.shuffle();
		dealMyCards();
	}
	
	//returns the size of the board
	public int size() 
	{
		return cards.length;
	}

	//returns true if board is empty, false otherwise
	public boolean isEmpty() 
	{
		for (int k = 0; k < cards.length; k++) 
		{
			if (cards[k] != null)
				return false;
		}
		return true;
	}

	//deal card to kth position of the board or set to nullif deck is empty
	public void deal(int k) 
	{
		cards[k]=deck.returnCard(0);
		deck.removeCard(0);
	}

	//returns the number of undealt card left in the deck
	public int deckSize() 
	{
		return deck.size();
	}

	//return the card at position k on the board
	public Card cardAt(int k) 
	{
		return cards[k];
	}

	//replaces selected cards on board by dealing new cards
	//@param selectedCards is a list of the INDICES of the cards to be replaced
	public void replaceSelectedCards(ArrayList<Integer> selectedCards) 
	{
		for(int i = 0; i < selectedCards.size(); i++)
		{
			int k = selectedCards.get(i);
			deal(k);
		}
	}

	//return a list that contains the INDEXES of the non-null cards on board 
	public ArrayList<Integer> cardIndexes() 
	{
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int k = 0; k < cards.length; k++) 
		{
			//if there is a card at index k (rather than an empty space),
			//then add its index to the list
			if (cards[k] != null) {
				selected.add(k);
			}
		}
		return selected;
	}

	//string representation of the board
	public String toString() 
	{
		String s = "";
		for (int k = 0; k < cards.length; k++) {
			s = s + k + ": " + cards[k] + "\n";
		}
		return s;
	}

	//returns true if game is won (board and deck are empty)
	public boolean gameIsWon() 
	{
		if (deck.size()==0) 
		{
			for (Card c: cards) 
			{
				if (c != null)
					return false;
			}
			return true;
		}
		return false;
	}

	//deal cards to this board to start the game
	private void dealMyCards() 
	{
		for (int k = 0; k < cards.length; k++) 
		{
			cards[k]=deck.returnCard(0);
			deck.removeCard(0);
		}
	}
	
	//determine if select cards form a valid group for removal 
	//@param selectedCards the list of the indices of the selected cards
	public abstract boolean isLegal(ArrayList<Integer> selectedCards);

	//determine if any legal plays are left on the board
	public abstract boolean anotherPlayIsPossible();

	
}
