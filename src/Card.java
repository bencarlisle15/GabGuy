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
	
	public String toString()
	{
		String n;
		if (num==1)
			n="Ace";
		else if (num==11)
			n="Jack";
		else if (num==12)
			n="Queen";
		else if (num==13)
			n="King";
		else
			n=String.valueOf(num);
		return "The " + n + " of " + type;
	}

}
