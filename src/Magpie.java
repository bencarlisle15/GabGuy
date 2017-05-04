import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Magpie
{
	private String name;
	private Calculator calc=new Calculator();
	private RPSMag rps;
	private BlackjackImpApp b;
	public int code=0;

	public String getResponse(String s)
	{
		String statement=s.toLowerCase();
		String response="";
		
		if (code==1)
		{
			if (findKeyword(statement,"rock")||findKeyword(statement,"paper")||findKeyword(statement,"scissors"))
			{
				rps=new RPSMag();
				response="Rock, Paper, Scissors, Shoot!";
				code=2;
			}
			else if (findKeyword(statement,"blackjack"))
			{
				b=new BlackjackImpApp();
				response=b.getNext(statement);
				code=3;
			}
			else if (findKeyword(statement,"elevens"))
			{
				Board e=new ElevensBoard();
				CardGameGUI c=new CardGameGUI(e);
				c.displayGame();
				code=0;
				response="Good game!";
			}
			else if (findKeyword(statement,"thirteens"))
			{
				Board e=new ThirteensBoard();
				CardGameGUI c=new CardGameGUI(e);
				c.displayGame();
				code=0;
				response="Good game!";
			}
			else
			{
				response=("Sorry, that game is not supported.");
				code=0;
			}
		}
		else if (code==2)
		{
			response=rps.game(statement)+"\n";
			if (name==null)
				response+="Good game!";
			else
				response+="Good game, "+name+"!";
			code=0;
		}
		else if (code==3)
		{
			response=b.getNext(statement);
			if (b.c==100)
				code=0;
		}
		else if (code==4)
		{
			if (statement.indexOf("n")>=0)
				response="Okay";
			else
			{	
				Scanner is=new Scanner(getClass().getResourceAsStream("files/README.md"));
				while (is.hasNextLine())
				{
					response+=is.nextLine()+"\n";
				}
				is.close();
			}
			code=0;
		}
		else
		{
			calc.eq=statement;
			if (calc.checkEq())
				response=statement+"="+calc.equationSolver(statement);
			else if (statement.matches("(.*)my name is ([a-zA-Z]| |[.]|-)+"))
				response=name(statement);
			else if (findKeyword(statement,"my name is"))
				response="I doubt that's your real name";
			else if (findKeyword(statement,"game"))
			{
				response="What game would you like to play?";
				code=1;
			}
			else if (findKeyword(statement,"help"))
			{
				response="Do you need help?";
				code=4;
			}
			else if (statement.matches(
					"(.*)(((who|what|where|why|how|when) is)|(what's|who's|where's|why's|how's|when's)) (.)+"))
						response=whatIs(statement);
			else if (findKeyword(statement,"time"))
				response=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			else if (findKeyword(statement,"date")||findKeyword(statement,"day"))
				response=new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
			else if (statement.matches("(.*)i (([a-zA-Z]| )+) you"))
				response="Why do you "+ statement.substring((statement.indexOf("i ")+2),
				statement.length()-4)+ " me";
			else if (findKeyword(statement,"I want to"))
				response="Why do you want to "+ statement.substring(Math.min(statement.indexOf("I want to")+11,
				statement.length()));
			else if (findKeyword(statement,"I want"))
				response="But would you really be happy if you had "+
				statement.substring(statement.indexOf("I want ")+8);
			else if (statement.equals(" ")||statement.equals(""))
				response="Well that's no fun";
			else if (findKeyword(statement,"brother"))
				response = "How old is your brother";
			else if (findKeyword(statement,"sister"))
				response = "How old is your sister";
			else if (findKeyword(statement,"mother"))
				response = "How old is your mother";
			else if (findKeyword(statement,"father"))
				response = "How old is your father";
			else if (findKeyword(statement,"family"))
				response= "Cool, I never knew my family";
			else if (findKeyword(statement,"cat")||(findKeyword(statement,"dog")))
				response="I love animals";
			else if (statement.equals("yes"))
				response="I knew it";
			else if (statement.equals("okay"))
				response="What would you like to talk about";
			else if (findKeyword(statement,"y")||findKeyword(statement,"why"))
				response="Because that's what the government wants you to think";
			else if (findKeyword(statement,"sad")||(findKeyword(statement,"bad")))
				response="Just like my grades";
			else if (findKeyword(statement,"knew"))
				response="Prove it";
			else if (statement.substring(0,Math.min(4,statement.length())).equals("ping"))
				response=ping();
			else if (statement.matches(".* you"))
				response=statement+" too";
			else if (statement.equals("no"))
				response="Why so negative?";
			else if (statement.equals("bye")||findKeyword(statement,"exit"))
				System.exit(0);
			else if (statement.equals("hello")||statement.equals("hi"))
			{
				if (name==null)
					response="We meet again";
				else
					response="Hello, " + name;
			}
			else
				response = getRandomResponse();
		}
		return response;
	}
	
	private String getRandomResponse()
	{
		switch ((int)(4*Math.random()+1))
		{
		case 1:
			return "Oh really";
		case 2:
			return "Not since the accident";
		case 3:
			return "Y tho";
		default:
			return "Okay";
		}
	}
	
	private boolean findKeyword(String phrase, String goal)
	{
		goal = goal.toLowerCase();
		String pattern = "([^a-zA-Z]|\\A)" + goal + "(\\z|[^a-zA-Z])";
		Pattern f = Pattern.compile(pattern);
		Matcher letters = f.matcher(phrase);
		return letters.find();
	}

	private String whatIs(String s)
	{
		int pos=s.indexOf("'s ")+3;
		if (pos==2||s.indexOf("is")<s.indexOf("'s ")&&s.indexOf("is")>=0)
			pos=s.indexOf(" is ")+4;
		String whatIs= s.substring(pos);
		String ans="";
		whatIs=whatIs.trim();
		int p=0;
		if (whatIs.substring(0,Math.min(1,whatIs.length())).equals("a")&&!whatIs.equals("a"))
			whatIs=whatIs.substring(2);
		else if (whatIs.substring(0,Math.min(3,whatIs.length())).equals("the"))
			whatIs=whatIs.substring(4);
		for (int i=0;i<whatIs.length();i++)
		{
			if (whatIs.substring(i,Math.min(i+1,whatIs.length())).equals(" "))
			{
				ans+=whatIs.substring(p,i)+"+";
				p=++i;
			}
			else if (whatIs.substring(i,Math.min(i+1,whatIs.length())).equals("'"))
			{
				ans+=whatIs.substring(p,i)+"%27";
				p=++i;
			}
		}
		ans+=whatIs.substring(p);
		return new InfoFinder().create(ans);
	}
	
	private String ping()
	{
		try
		{
			LineNumberReader lr = new LineNumberReader(new InputStreamReader(
			new URL("https://www.google.com").openConnection().getInputStream()));
			if (lr.ready())
				return "Test successfull";
			else
				throw new IOException();
		}
		catch (IOException e)
		{
			return "Test failed, could not connect";
		}

	}
	
	private String name(String statement)
	{
		name=statement.substring(statement.indexOf("my name is ")+11,
		statement.indexOf("my name is ")+12).toUpperCase();
		for (int i=statement.indexOf("my name is ")+13;i<=statement.length();i++)
			if ((statement.substring(i-1,i).equals(" ")||statement.substring(i-1,i).equals(".")
			||statement.substring(i-1,i).equals("-"))&&i<statement.length())
			{
				name+=statement.substring(i-1,i) + statement.substring(i,i+1).toUpperCase();
				i++;
			}
			else
				name+=statement.substring(i-1,i).toLowerCase();
	return "Hello, " + name;
	}
}