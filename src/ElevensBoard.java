import java.util.ArrayList;

public class ElevensBoard extends CardBoard 
{

	private static final int BOARD_SIZE = 9; //number of cards on the board
	private static final String[] RANKS ={"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
	private static final String[] SUITS = {"spades", "hearts", "diamonds", "clubs"};
	private static final int[] POINT_VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0};

	public ElevensBoard() 
	{
		super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES);
	}

	/**
	 * Determines if the selected cards form a valid group for removal.
	 * In Elevens, the legal groups are (1) a pair of non-face cards
	 * whose values add to 11, and (2) a group of three cards consisting of
	 * a jack, a queen, and a king in some order.
	 * @param selectedCards the list of the indices of the selected cards.
	 * @return true if the selected cards form a valid group for removal;
	 *         false otherwise.
	 */
	@Override
	public boolean isLegal(ArrayList<Integer> selectedCards) {
		if (selectedCards.size() == 2) {
			return findPairSum11(selectedCards).size() > 0;
		} 
		else if (selectedCards.size() == 3) {
			return findJQK(selectedCards).size() > 0;
		} 
		else {
			return false;
		}
	}

	/**
	 * Determine if there are any legal plays left on the board.
	 * In Elevens, there is a legal play if the board contains
	 * (1) a pair of non-face cards whose values add to 11, or (2) a group
	 * of three cards consisting of a jack, a queen, and a king in some order.
	 * @return true if there is a legal play left on the board;
	 *         false otherwise.
	 */
	@Override
	public boolean anotherPlayIsPossible() {
		ArrayList<Integer> cIndexes = cardIndexes();
		return findPairSum11(cIndexes).size() > 0 || findJQK(cIndexes).size() > 0;
	}

	/**
	 * Look for an 11-pair in the selected cards.
	 * @param selectedCards selects a subset of this board.  It is list
	 *                      of indexes into this board that are searched
	 *                      to find an 11-pair.
	 * @return a list of the indexes of an 11-pair, if an 11-pair was found;
	 *         an empty list, if an 11-pair was not found.
	 */
	private ArrayList<Integer> findPairSum11(ArrayList<Integer> selectedCards) 
	{
		ArrayList<Integer> ans = new ArrayList<Integer>();
		outer:
		for (int i=0;i<selectedCards.size();i++)
		{
			for (int p=0;p<i;p++)
				if (cardAt(selectedCards.get(i)).getNum()+cardAt(selectedCards.get(p)).getNum()==11)
				{
					ans.add(p);
					ans.add(i);
					break outer;
				}
		}
		return ans;
		
	}
	
	/**
	 * Look for a JQK in the selected cards.
	 * @param selectedCards selects a subset of this board.  It is list
	 *                      of indexes into this board that are searched
	 *                      to find a JQK group.
	 * @return a list of the indexes of a JQK, if a JQK was found;
	 *         an empty list, if a JQK was not found.
	 */
	private ArrayList<Integer> findJQK(ArrayList<Integer> selectedCards) 
	{
		ArrayList<Integer> ans = new ArrayList<Integer>();
		boolean j=false,q=false,k=false;
		for (int i=0;i<selectedCards.size();i++)
		{
			if (cardAt(selectedCards.get(i)).getRank().equalsIgnoreCase("jack"))
				{
					j=true;
					ans.add(selectedCards.get(i));
				}
			else if (cardAt(selectedCards.get(i)).getRank().equalsIgnoreCase("queen"))
			{
				q=true;
				ans.add(selectedCards.get(i));
			}
			else if (cardAt(selectedCards.get(i)).getRank().equalsIgnoreCase("king"))
			{
				k=true;
				ans.add(selectedCards.get(i));
			}
			if (j&&q&&k)
				break;
		}
		if (!(j&&q&&k))
			ans=new ArrayList<Integer>();
		return ans;
				
	}
}
