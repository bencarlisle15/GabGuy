public class Card
{

	private int num;
	private String type;

	public Card(int n, String t)
	{
		num=n;
		type=t;
	}
	
	public int returnNum()
	{
		return num;
	}
	
	public String returnType()
	{
		return type;
	}
	
	public String returnRank()
	{
		String r;
		if (num==1)
			r="Ace";
		else if (num==11)
			r="Jack";
		else if (num==12)
			r="Queen";
		else if (num==13)
			r="King";
		else
			r=String.valueOf(num);
		return r;
	}
	
	public String toString()
	{
		return "The " + returnRank() + " of " + type;
	} 

}
