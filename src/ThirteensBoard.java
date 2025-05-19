import java.util.ArrayList;
public class ThirteensBoard extends CardBoard {
  // private instance variables
  private static final int BOARD_SIZE = 10; // number of cards on the board
  private static final String[] RANKS = {"ace",  "2",     "3",   "4", "5",
                                         "6",    "7",     "8",   "9", "10",
                                         "jack", "queen", "king"};
  private static final String[] SUITS = {"spades", "hearts", "diamonds",
                                         "clubs"};
  private static final int[] POINT_VALUES = {1, 2, 3,  4,  5,  6, 7,
                                             8, 9, 10, 11, 12, 0};

  public ThirteensBoard() { super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES); }

  /**
   * Determines if the selected cards form a valid group for removal.
   * In Elevens, the legal groups are (1) a pair of non-face cards
   * whose values add to 11, and (2) a group of three cards consisting of
   * a jack, a queen, and a king in some order.
   * @param selectedCards the list of the indices of the selected cards.
   * @return true if the selected cards form a valid group for removal;
   *         false otherwise.
   */
  public boolean isLegal(ArrayList<Integer> selectedCards) {
    if (selectedCards.size() == 1)
      return findKing(selectedCards).size() == 1;
    else if (selectedCards.size() == 2)
      return findPair(selectedCards).size() == 2;
    return false;
  }

  /**
   * Determine if there are any legal plays left on the board.
   * In Elevens, there is a legal play if the board contains
   * (1) a pair of non-face cards whose values add to 11, or (2) a group
   * of three cards consisting of a jack, a queen, and a king in some order.
   * @return true if there is a legal play left on the board;
   *         false otherwise.
   */
  public boolean anotherPlayIsPossible() {
    ArrayList<Integer> a = cardIndexes();
    return findKing(a).size() > 0 || findPair(a).size() > 0;
  }

  /**
   * Look for a King in the selected cards.
   * @param selectedCards selects a subset of this board.  It is list
   *                      of indexes into this board that are searched
   *                      to find a King.
   * @return a list of the indexes of a King, if a King was found;
   *         an empty list, if a King was not found.
   */
  private ArrayList<Integer> findKing(ArrayList<Integer> selectedCards) {
    ArrayList<Integer> ans = new ArrayList<Integer>();
    for (int i = 0; i < selectedCards.size(); i++)
      if (cardAt(selectedCards.get(i)).getRank().equalsIgnoreCase("king")) {
        ans.add(i);
        break;
      }
    return ans;
  }

  /**
   * Look for a 10-pair in the selected cards.
   * @param selectedCards selects a subset of this board.  It is list
   *                      of indexes into this board that are searched
   *                      to find a 10-pair.
   * @return a list of the indexes of an 10-pair, if a 10-pair was found;
   *         an empty list, if a 10-pair was not found.
   */
  private ArrayList<Integer> findPair(ArrayList<Integer> selectedCards) {
    ArrayList<Integer> ans = new ArrayList<Integer>();
  outer:
    for (int i = 0; i < selectedCards.size(); i++) {
      for (int p = 0; p < i; p++)
        if (cardAt(selectedCards.get(i)).getNum() +
                cardAt(selectedCards.get(p)).getNum() ==
            13) {
          ans.add(p);
          ans.add(i);
          break outer;
        }
    }
    System.out.println(ans.size());
    return ans;
  }
}