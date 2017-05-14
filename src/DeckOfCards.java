import java.util.ArrayList;
public class DeckOfCards
{
	private ArrayList<Card> deck;
	private ArrayList<Card> perfect = new ArrayList<Card>();
	private String[] types={"Spades","Clubs","Hearts","Diamonds"};
	
	public DeckOfCards(boolean preShuffle)
	{
		for (int i=0;i<types.length;i++)
			for (int t=1;t<=13;t++)
				perfect.add(new Card(t,types[i]));
	if (preShuffle)
		shuffle();
	else
		deck=perfect;
	}
	
	public void shuffle()
	{
		deck=new ArrayList<Card>();
		Card newCard;
		for (int i=0;i<perfect.size();i++)
		{
			newCard=newCard();
			if (shuffleCheck(i,newCard))
				deck.add(newCard);
			else
				i--;
		}	
			
	}
	
	private boolean shuffleCheck(int i, Card newCard)
	{
		for (int t=i-1;t>=0;t--)
			if (deck.get(t).returnType().equals(newCard.returnType())&&deck.get(t).returnNum()==newCard.returnNum())
				return false;
		return true;
	}
	
	private Card newCard()
	{
		return new Card((int)(Math.random()*(13)+1),types[(int)(Math.random()*(types.length))]);
	}
	
	public Card returnCard(int p)
	{
		if (deck.size()>0)
			return deck.get(p);
		return null;
	}
	
	public void removeCard(int p)
	{
		if (deck.size()>0)
			deck.remove(p);
	}
	
	public int size()
	{
		return deck.size();
	}

}
