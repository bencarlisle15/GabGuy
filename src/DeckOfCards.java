import java.util.ArrayList;
public class DeckOfCards {
    // private instance variables
    private ArrayList<Card> deck;
    private String[] types = {"Spades", "Clubs", "Hearts", "Diamonds"};

    public DeckOfCards(boolean preShuffle) {
        // shuffles the deck if wished
        if (preShuffle)
            shuffle();
        else {
            deck = new ArrayList<Card>();
            // adds the cards in order
            for (int i = 0; i < types.length; i++)
                for (int t = 1; t <= 13; t++)
                    deck.add(new Card(t, types[i]));
        }
    }

    public void shuffle() {
        deck = new ArrayList<Card>();
        Card newCard;
        for (int i = 0; i < 52; i++) {
            // adds a new card if it doesn't already exist in the deck
            newCard = newCard();
            if (shuffleCheck(i, newCard))
                deck.add(newCard);
            else
                i--;
        }
    }

    private boolean shuffleCheck(int i, Card newCard) {
        // checks to see if the deck already has the card
        for (int t = i - 1; t >= 0; t--)
            if (deck.get(t).getType().equals(newCard.getType()) &&
                    deck.get(t).getNum() == newCard.getNum())
                return false;
        return true;
    }

    private Card newCard() {
        // returns a random card
        return new Card((int)(Math.random() * (13) + 1),
                                        types[(int)(Math.random() * (types.length))]);
    }

    public Card returnCard(int p) {
        // return card at p
        if (deck.size() > p && p >= 0)
            return deck.get(p);
        return null;
    }

    public void removeCard(int p) {
        // removes card at p
        if (deck.size() > p && p >= 0)
            deck.remove(p);
    }

    public int size() {
        // returns deck size
        return deck.size();
    }
}
