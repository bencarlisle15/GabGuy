import java.util.ArrayList;
public class Blackjack
{
	private DeckOfCards deck;
	private boolean hasAce;
	private final int STARTINGBET=5;
	private final int STARTINGAMOUNT=100;
	private ArrayList<Card> hand;
	private ArrayList<Card> cpuHand;
	private int amount=STARTINGAMOUNT;
	private int bet=STARTINGBET;
	private boolean desire;
	private int code=-1;
	
	public String getNext(String s)
	{
		code++;
		switch (code)
		{
			case 0:
				return case0();
			case 1:
				return case1(s);
			case 2:
				return case2(s);
			case 3:
				return case3(s);
			default:
				return "An error has occured";
		}
	}
	
	public int getCode()
	{
		return code;
	}
	
	private String case0()
	{
		return questionDesire(true);
	}
	private String case1(String s)
	{
		desire=desire(s);
		if (desire)
		{
			deck=new DeckOfCards(true);
			hand=new ArrayList<Card>();
			cpuHand=new ArrayList<Card>();
			hasAce=false;
			if (amount<STARTINGBET)
			{
				code=101;
				return gameOver()+"\n"+insufficientFunds();
			}
			else
				return bet(amount);
		}
		code=100;
		return gameOver();
	}
	
	private String case2(String s)
	{
		if (s.equalsIgnoreCase("all in"))
			bet=amount;
		else
		{
			try
			{
				bet=Integer.parseInt(s);
				if (bet<STARTINGBET)
					return lowBet()+"\n"+questionBet();
				else if (bet>amount)
					return insufficientFunds();
			}
			catch (NumberFormatException e)
			{
				return invalidSelection();
			}
		}
		String response="";
		amount-=bet;
		hand.add(Hit());
		response+=draw(true,hand.get(hand.size()-1))+"\n";
		hand.add(Hit());
		response+=draw(true,hand.get(hand.size()-1))+"\n";
		response+=onHand(true,hand,hasAce)+"\n";
		response+=questionMove();
		return response;
	}

	private String case3(String move)
	{
		String response="";
		if (move.equals("fold"))
			return case4();
		else if (move.equals("hit"))
		{	
			hand.add(Hit());
			response+=draw(true,hand.get(hand.size()-1))+"\n";
			response+=onHand(true,hand,hasAce)+"\n";
			if (cardTotal(hand)>=21)
				return response+case4();
			code--;
			return response+questionMove();
		}
		else if (move.equals("double down"))
		{
			if (hand.size()==2&&amount>=bet)
			{
				amount-=bet;
				bet*=2;
				hand.add(Hit());
				response+=draw(true,hand.get(hand.size()-1))+"\n";
				return response+case4();
			}
			else
				return insufficientFunds();
		}
		else
			return invalidSelection();
	}
	private String case4()	
	{
		String response="";
		if (hasAce&&cardTotal(hand)+10<=21)
		{
			ArrayList<Card> eHand=hand;
			eHand.add(new Card(10,"clubs"));
			response+=onHand(true,eHand,false)+"\n";
		}
		else
			response+=onHand(true,hand,false)+"\n";
		cpuHand.add(Hit());
		response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
		cpuHand.add(Hit());
		response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
		while (cardTotal(cpuHand)<21)
		{
			if (cardTotal(cpuHand)<17)
			{
				cpuHand.add(Hit());
				response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
			}
			else
				break;
		}
		response+=onHand(false,cpuHand,false)+"\n";
		int cardTotal=cardTotal(hand);
		if (hasAce&&cardTotal(hand)+10<=21)
			cardTotal+=10;
		int cpuCardTotal=cardTotal(cpuHand);
		response+=onHand(true,hand,false)+"\n";
		if (cardTotal==21&&cpuCardTotal!=21
		||cardTotal<21&&cpuCardTotal>21
		||cardTotal<21&&cardTotal>cpuCardTotal)
		{
			response+=win()+"\n";
			amount+=(2*bet);
		}
		else if (cpuCardTotal==cardTotal
		||cpuCardTotal>21&&cardTotal>21)
		{
			response+=tie()+"\n";
			amount+=bet;
		}
		else
			response+=lose()+"\n";
		code=0;
		response+=amount(amount)+"\n";
		response+=questionDesire(false);
		return response;
	}
	
	private Card Hit()
	{
		Card c=deck.returnCard(0);
		deck.removeCard(0);
		if (c.returnNum()==1)
			hasAce=true;
		return c;
	}
	
	protected int cardTotal(ArrayList<Card> h)
	{
		int sum=0;
		for (Card c:h)
			sum+=Math.min(10,c.returnNum());
		return sum;
	}

	private String questionDesire(boolean firstTime)
	{
		if (firstTime)
			return "Would you like to play Blackjack?";
		else
			return "Would you like to play again?";
	}
	
	private boolean desire(String s)
	{
		if (s.equalsIgnoreCase("no"))
			return false;
		return true;
	}
	
	private String insufficientFunds()
	{
		code--;
		return "Insufficient funds";
	}
	
	private String invalidSelection()
	{
		code--;
		return "Invalid selection";
	}
	

	private String questionBet()
	{
		return "How much would you like to bet";
	}
	
	private String draw(boolean user,Card c)
	{
		if (user)
			return "You drew the " + c;
		else
			return "The dealer drew the " +c;
	}
	
	private String onHand(boolean user, ArrayList<Card> hand, boolean hasAce)
	{	
		if (user)
		{
			if (hasAce)
			{
				if (cardTotal(hand)<11)
					return "You have a total of " + cardTotal(hand) + " or " + (cardTotal(hand)+10);
				else if (cardTotal(hand)>11)
					return "You have a total of " + cardTotal(hand);
				else
					return "You have a total of 21";
			}
			else
				return "You have a total of " + cardTotal(hand);
		}
		else
			return "The dealer has a total of " + cardTotal(hand);
	}

	private String questionMove()
	{
		return "What would you like to do?";
	}
	
	private String win()
	{
		return "Congratulations, you win!";
	}
	
	private String tie()
	{
		return "You tied, that's okay.";
	}
	
	private String lose()
	{
		return "You lost, better luck next time.";
	}
	
	private String lowBet()
	{
		code--;
		return "Your bet is too low, please raise it";
	}

	private String amount(int amount)
	{
		return "You have " + amount;
	}
	private String bet(int amount)
	{
		return "You have " + amount +", how much would you like to bet";
	}
	
	private String gameOver()
	{
		return "GAME OVER!";
	}
}