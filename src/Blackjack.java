import java.util.ArrayList;
public class Blackjack
{
	//private instance variables
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
		//does the next set of statements
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
	
	//returns the current code
	public int getCode()
	{
		return code;
	}
	
	//first case response and asks if the user wants to play for the first time
	private String case0()
	{
		return questionDesire(true);
	}
	
	//second case response
	private String case1(String s)
	{
		//checks desire
		desire=desire(s);
		if (desire)
		{
			//initializes instance variables
			deck=new DeckOfCards(true);
			hand=new ArrayList<Card>();
			cpuHand=new ArrayList<Card>();
			hasAce=false;
			//checks to see if the user has enough money
			if (amount<STARTINGBET)
			{
				//code to tell the calling class to end the game
				code=101;
				return gameOver()+"\n"+insufficientFunds();
			}
			else
			//tells the user how much they have and asks how much they want to bet
				return bet(amount);
		}
		//tells the calling class to end the game
		code=100;
		return gameOver();
	}
	
	//third case response
	private String case2(String s)
	{
		//checks various response
		if (s.equalsIgnoreCase("all in"))
			bet=amount;
		else
		{
			try
			{
				//converts the response to an int
				bet=Integer.parseInt(s);
				//checks to see if the bet is large enough
				if (bet<STARTINGBET)
					return lowBet()+"\n"+questionBet();
				//checks to see if the bet is too large
				else if (bet>amount)
					return insufficientFunds();
			}
			//checks for invalid bet
			catch (NumberFormatException e)
			{
				return invalidSelection();
			}
		}
		String response="";
		//takes the bet away from the amount
		amount-=bet;
		//adds a card
		hand.add(Hit());
		response+=draw(true,hand.get(hand.size()-1))+"\n";
		//adds a card
		hand.add(Hit());
		response+=draw(true,hand.get(hand.size()-1))+"\n";
		//tells the user how much they have
		response+=onHand(true,hand,hasAce)+"\n";
		//asks the user what they want to do
		response+=questionMove();
		return response;
	}

	private String case3(String move)
	{
		String response="";
		//ends the turn if they want to fold
		if (move.equals("fold"))
			return case4();
		else if (move.equals("hit"))
		{	
			//adds a card and tells them how much they have
			hand.add(Hit());
			response+=draw(true,hand.get(hand.size()-1))+"\n";
			response+=onHand(true,hand,hasAce)+"\n";
			//ends the turn if they have 21 or more
			if (cardTotal(hand)>=21)
				return response+case4();
			//subtracts one from the code so that case3 will be repeated on the next turn
			code--;
			//asks the user what they want to do
			return response+questionMove();
		}
		else if (move.equals("double down"))
		{
			//can only double down if it's their first move and they have enough money to double their bet
			if (hand.size()==2&&amount>=bet)
			{
				//subtracts the original bet and then multiples it by two
				amount-=bet;
				bet*=2;
				//adds a card and ends the turn
				hand.add(Hit());
				response+=draw(true,hand.get(hand.size()-1))+"\n";
				return response+case4();
			}
			//if they don't have enough money they need to redo their pick
			else
				return insufficientFunds();
		}
		//if none of the above were typed
		else
			return invalidSelection();
	}
	private String case4()	
	{
		String response="";
		//if they have an ace the best option is picked
		if (hasAce&&cardTotal(hand)+10<=21)
		{
			//creates a fake hand for the purposes of simulating the 11 power of an ace and adds a card of no suit worth ten
			ArrayList<Card> eHand=hand;
			eHand.add(new Card(10,""));
			//how much the user has in the fake hand
			response+=onHand(true,eHand,false)+"\n";
		}
		else
			//how much the user has
			response+=onHand(true,hand,false)+"\n";
		//adds a two card minimum to the cpu hand
		cpuHand.add(Hit());
		response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
		cpuHand.add(Hit());
		response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
		//only adds cards when the cpu hand has less than ten
		while (cardTotal(cpuHand)<21)
		{
			//only adds if the hand is less than 17 (general rule of thumb number for blackjack)
			if (cardTotal(cpuHand)<17)
			{
				cpuHand.add(Hit());
				response+=draw(false,cpuHand.get(cpuHand.size()-1))+"\n";
			}
			else
				break;
		}
		//how much the cpu hand has
		response+=onHand(false,cpuHand,false)+"\n";
		int cardTotal=cardTotal(hand);
		//if the user has an ace adds ten to the integer card total
		if (hasAce&&cardTotal(hand)+10<=21)
			cardTotal+=10;
		int cpuCardTotal=cardTotal(cpuHand);
		response+=onHand(true,hand,false)+"\n";
		//win scenarios
		if (cardTotal==21&&cpuCardTotal!=21
		||cardTotal<21&&cpuCardTotal>21
		||cardTotal<21&&cardTotal>cpuCardTotal)
		{
			//tells the user they have won and adds their bet plus the cpu bet to their ammount
			response+=win()+"\n";
			amount+=(2*bet);
		}
		//tie scenarios
		else if (cpuCardTotal==cardTotal
		||cpuCardTotal>21&&cardTotal>21)
		{
			//tells the user they have tied and returns their bet
			response+=tie()+"\n";
			amount+=bet;
		}
		else
			//tells the user they have lost
			response+=lose()+"\n";
		//prepares to restart the game
		code=0;
		//tells the user how much they have and if they want to play again
		response+=amount(amount)+"\n";
		response+=questionDesire(false);
		return response;
	}
	
	private Card Hit()
	{
		//adds the next card to their deck
		Card c=deck.returnCard(0);
		deck.removeCard(0);
		//the user has an ace if the card is 1
		if (c.getNum()==1)
			hasAce=true;
		return c;
	}
	
	private int cardTotal(ArrayList<Card> h)
	{
		//adds up the total of the deck
		int sum=0;
		for (Card c:h)
			//jack, queen, and king count for ten
			sum+=Math.min(10,c.getNum());
		return sum;
	}

	private String questionDesire(boolean firstTime)
	{
		//questions what the user would like to do
		if (firstTime)
			return "Would you like to play Blackjack?";
		else
			return "Would you like to play again?";
	}
	
	private boolean desire(String s)
	{
		//responds to their desire
		if (s.equalsIgnoreCase("no"))
			return false;
		return true;
	}
	
	private String insufficientFunds()
	{
		//restarts the last move
		code--;
		return "Insufficient funds";
	}
	
	private String invalidSelection()
	{
		//restarts the last move
		code--;
		return "Invalid selection";
	}
	

	private String questionBet()
	{
		//asks for their bet
		return "How much would you like to bet";
	}
	
	private String draw(boolean user,Card c)
	{
		//tells the most recent card drawn
		if (user)
			return "You drew the " + c;
		else
			return "The dealer drew the " +c;
	}
	
	private String onHand(boolean user, ArrayList<Card> hand, boolean hasAce)
	{
		//returns the current amount from the specified hand
		if (user)
		{
			//tells the user their options if they have an ace
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
		//asks for the next move
		return "What would you like to do?";
	}
	
	private String win()
	{
		//win message
		return "Congratulations, you win!";
	}
	
	private String tie()
	{
		//tie message
		return "You tied, that's okay.";
	}
	
	private String lose()
	{
		//loss message
		return "You lost, better luck next time.";
	}
	
	private String lowBet()
	{
		//bet is too low restarts turn
		code--;
		return "Your bet is too low, please raise it";
	}

	private String amount(int amount)
	{
		//returns amount
		return "You have " + amount;
	}
	private String bet(int amount)
	{
		//returns amount and asks for bet
		return "You have " + amount +", how much would you like to bet";
	}
	
	private String gameOver()
	{
		//GAME OVER!
		return "GAME OVER!";
	}
}