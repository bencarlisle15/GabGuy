public class RPSMag
{
	public String game(String statement)
	{
		String response;
		int move= (int) (3*Math.random()+1);
		int yourMove=0;
		String yours=statement;
		if (yours.equalsIgnoreCase("rock"))
			yourMove=1;
		else if (yours.equalsIgnoreCase("scissors"))
			yourMove=2;
		else if (yours.equalsIgnoreCase("paper"))
			yourMove=3;
		else
			return "That is not a valid entry, goodbye!";
		if (yourMove==move)
			response="I drew " + yours + ", it is a tie";
		else if (yourMove==1&&move==2)
			response="I drew scissors, you win!";
		else if (yourMove==1&&move==3)
			response="I drew paper, I win!";
		else if (yourMove==2&&move==1)
			response="I drew rock, I win!";
		else if (yourMove==2&&move==3)
			response="I drew paper, you win!";
		else if (yourMove==3&&move==1)
			response="I drew rock, you win!";
		else if (yourMove==3&&move==2)
			response="I drew scissors, I win!";
		else
		{
			response="Ahhh there is a system error save me please!";
			System.exit(1);
		}
		return response;
	}
}
