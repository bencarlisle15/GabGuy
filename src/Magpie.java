import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Magpie extends Thread
{
	private String name;
	private Blackjack b;
	public int code=0;
	private HashMap<String,String> others=new HashMap<String,String>();
	private HashMap<String,ArrayList<String>> list=new HashMap<String,ArrayList<String>>();
	public String statement;
	public String response;
	
	public String getResponse(String s)
	{
		setStatement(s);
		run();
		return getResponse();
	}
	
	public String getResponse()
	{
		if (response!=null)
		{
			String temp=new String(response);
			response=null;
			return temp;
		}
		return response;
	}
	
	public void setStatement(String s)
	{
		statement=s.toLowerCase();
	}
	
	public void run()
	{
		response=null;
		if (code==1)
		{
			if (findKeyword(statement,"rock")||findKeyword(statement,"paper")||findKeyword(statement,"scissors"))
			{
				response="Rock, Paper, Scissors, Shoot!";
				code=2;
			}
			else if (findKeyword(statement,"blackjack"))
			{
				b=new Blackjack();
				response=b.getNext(statement);
				code=3;
			}
			else if (findKeyword(statement,"elevens"))
				new CardBoardGUI(new ElevensBoard()).displayGame();
			else if (findKeyword(statement,"thirteens"))
				new CardBoardGUI(new ThirteensBoard()).displayGame();
			else if (findKeyword(statement,"tic")||findKeyword(statement,"tac")||findKeyword(statement,"toe"))
				new TicTacToe().create();
			else
			{
				response=("Sorry, that game is not supported.");
				code=0;
			}
			if (code==1&&response==null)
			{
				code=0;
				response=goodGame();
			}
		}
		else if (code==2)
		{
			response=rps(statement)+"\n"+goodGame();
			code=0;
		}
		else if (code==3)
		{
			response=b.getNext(statement);
			if (b.getCode()==100)
				code=0;
		}
		else if (code==4)
		{
			if (statement.indexOf("n")>=0)
				response="Okay";
			else
			{	
				Scanner is=new Scanner(getClass().getResourceAsStream("files/help.md"));
				response="";
				while (is.hasNextLine())
					response+=is.nextLine()+"\n";
				is.close();
			}
			code=0;
		}
		else
		{
			if (others.containsKey(statement))
				response=returnOthers(statement);
			else if (new Calculator().checkEq(statement))
				response="="+new Calculator().equationSolver(statement);
			else if (statement.matches("(.*)my name is ([a-zA-Z]| |[.]|-)+"))
				response=name(statement);
			else if (findKeyword(statement,"my name is"))
				response="I doubt that's your real name";
			else if (statement.matches("(.*)if i say (.+) then say (.+)"))
				response=addResponse(statement);
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
			else if (statement.matches("(.*)add (.+) to (.+)"))
				response=addToList(statement);
			else if (statement.matches("(.*)what's on (.+)"))
			{
				response="";
				if (list.containsKey(statement.substring(statement.indexOf("what's on")+10)))
					response+="Your list, " + statement.substring(statement.indexOf("what's on")+10) + ", has:\n";
				response+=returnList(statement);
			}
			else if (statement.matches(
					"(.*)(((who|what|where|why|how|when) is)|(what's|who's|where's|why's|how's|when's)) (.)+"))
						response=new InfoFinder().create(statement);
			else if (statement.matches("(.*)(who|what|where|why|how|when)((.)+)"))
				response=new InfoFinder().create(statement);
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
				response=name==null?"We meet again":"Hello, " + name;
			else if (statement.matches("(.*)do (.+)"))
				response="Why would I do "+statement.substring(statement.indexOf("do")+3);
			else
				response=getRandomResponse();
		}
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
	
	private String addResponse(String s)
	{
		others.put(s.substring(s.indexOf("if I say")+10,s.indexOf("then say")-1),s.substring(s.indexOf("then say")+9));
		return "Okay, keywords learned";
	}
	
	private String returnOthers(String s)
	{
		for (int i=0;i<others.size();i++)
			if (others.containsKey(s))
				return others.get(s);
		return "An error has occured";
	}
	
	private String addToList(String s)
	{
		if (list.containsKey(s.substring(s.indexOf("to")+3)))
			list.get(s.substring(s.indexOf("to")+3)).add(s.substring(s.indexOf("add")+4,s.indexOf("to")-1));
		else
		{
			ArrayList<String> ans=new ArrayList<String>();
			ans.add(s.substring(s.indexOf("add")+4,s.indexOf("to")-1));
			list.put(s.substring(s.indexOf("to")+3),ans);
		}
		return "Okay";
	}
	
	private String returnList(String s)
	{
		if (list.containsKey(s.substring(s.indexOf("what's on")+10)))
		{
			String ans="";
			ArrayList<String> l=list.get(s.substring(s.indexOf("what's on")+10));
			for (int i=0;i<l.size();i++)
				ans+=l.get(i)+"\n";
			return ans;
		}
		return "List not found";
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
	
	private String rps(String statement)
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
			response="An error has occured";
		return response;
	}
	
	private String goodGame()
	{
		String n=name!=null?(", "+name+"!"):"!";
		return "Good game"+n;
	}
}