public class Card {
    // private instance variables
    private int num;
    private String type;

    public Card(int n, String t) {
        // initializes the card with a number and a type/suit
        num = n;
        type = t;
    }

    public int getNum() {
        // returns the cards num
        return num;
    }

    public String getType() {
        // returns the cards type
        return type;
    }

    public String getRank() {
        // returns the cards num but changes it if it is a face card or ace
        String r;
        if (num == 1)
            r = "Ace";
        else if (num == 11)
            r = "Jack";
        else if (num == 12)
            r = "Queen";
        else if (num == 13)
            r = "King";
        else
            r = String.valueOf(num);
        return r;
    }

    public String toString() {
        // returns the rank and type
        return "The " + getRank() + " of " + type;
    }
}
