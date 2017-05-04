import java.util.ArrayList;
public class ThirteensBoard extends Board
{
	
	private static final int BOARD_SIZE = 10; //number of cards on the board
	private static final String[] RANKS ={"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
	private static final String[] SUITS = {"spades", "hearts", "diamonds", "clubs"};
	private static final int[] POINT_VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0};

	public ThirteensBoard()
	{
		super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES);
	}

	public boolean isLegal(ArrayList<Integer> selectedCards)
	{
		if (selectedCards.size()==1)
			return findKing(selectedCards).size()==1;
		else if (selectedCards.size()==2)
			return findPair(selectedCards).size()==2;
		return false;
	}

	public boolean anotherPlayIsPossible()
	{
		ArrayList<Integer> a = cardIndexes();
		return findKing(a).size()>0||findPair(a).size()>0;
	}
	
	public ArrayList<Integer> findKing(ArrayList<Integer> selectedCards)
	{
		ArrayList<Integer> ans=new ArrayList<Integer>();
		for (int i=0;i<selectedCards.size();i++)
			if (cardAt(selectedCards.get(i)).returnRank().equalsIgnoreCase("king"))
			{
				ans.add(i);
				break;
			}
		return ans;
	}
	
	public ArrayList<Integer> findPair(ArrayList<Integer> selectedCards)
	{
		ArrayList<Integer> ans = new ArrayList<Integer>();
		outer:
		for (int i=0;i<selectedCards.size();i++)
		{
			for (int p=0;p<i;p++)
				if (cardAt(selectedCards.get(i)).returnNum()+cardAt(selectedCards.get(p)).returnNum()==13)
				{
					ans.add(p);
					ans.add(i);
					break outer;
				}
		}
		System.out.println(ans.size());
		return ans;
	}

}