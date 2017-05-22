import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Magpie extends Thread
{
	//private instance variables
	private String name;
	private Blackjack b;
	private int code=0;
	private HashMap<String,String> others=new HashMap<String,String>();
	private HashMap<String,ArrayList<String>> list=new HashMap<String,ArrayList<String>>();
	private String statement;
	private String response;
	
	public String getResponse()
	{
		//returns the response and sets it to null
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
		//sets the statement and lowers it
		statement=s.toLowerCase();
	}
	
	public int getCode()
	{
		//returns the current code
		return code;
	}
	
	public void setCode(int c)
	{
		//sets the current code
		code=c;
	}
	
	//runs the magpie thread
	public void run()
	{
		response=null;
		//if the user wants to play a game
		if (code==1)
		{
			//looks for game
			if (findKeyword(statement,"rock")||findKeyword(statement,"paper")||findKeyword(statement,"scissors"))
			{
				response="Rock, Paper, Scissors, Shoot!";
				//sets code to the RPS code
				code=2;
			}
			else if (findKeyword(statement,"blackjack"))
			{
				//sets code to the Blackjack code
				b=new Blackjack();
				response=b.getNext(statement);
				code=3;
			}
			else if (findKeyword(statement,"elevens"))
				//starts elevens
				new CardBoardGUI(new ElevensBoard()).displayGame();
			else if (findKeyword(statement,"thirteens"))
				//starts thirteens
				new CardBoardGUI(new ThirteensBoard()).displayGame();
			else if (statement.contains("tic")||statement.contains("tac")||statement.contains("toe"))
			{
				//starts TTT
				TicTacToe t=new TicTacToe();
				t.create(t);
			}
			else if (statement.contains("brick")||statement.contains("breaker"))
			{
				//starts BB
				BrickBreaker b=new BrickBreaker();
				b.create(b);
				new Thread(b).start();
			}
			else
			{
				//game not found resets code to default
				response=("Sorry, that game is not supported.");
				code=0;
			}
			//if a new window game was started
			if (code==1&&response==null)
			{
				code=0;
				response=goodGame();
			}
		}
		else if (code==2)
		{
			//returns the cpu RPS response and end status
			response=rps(statement)+"\n"+goodGame();
			code=0;
		}
		else if (code==3)
		{
			//returns the next Blackjack response
			response=b.getNext(statement);
			//if the code is 100 the game is over
			if (b.getCode()==100)
				code=0;
		}
		//if the user hit f1
		else if (code==4)
		{
			//sees if they want help
			if (statement.indexOf("n")>=0)
				response="Okay";
			else
			{
				//new scanner for help file
				Scanner is=new Scanner(getClass().getResourceAsStream("files/help.md"));
				String ans="";
				//if another line exists
				while (is.hasNextLine())
					ans+=is.nextLine()+"\n";
				is.close();
				response=ans;
			}
			code=0;
		}
		else
		{
			//rather self explanatory if and response statements
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
				//checks for the list and returns what is on it
				if (list.containsKey(statement.substring(statement.indexOf("what's on")+10)))
					response+="Your list, " + statement.substring(statement.indexOf("what's on")+10) + ", has:\n";
				response+=returnList(statement);
			}
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
		//random responses
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
		//return if the goal can be found in the phrase
		return phrase.matches("([^a-zA-Z]|\\A)"+goal.toLowerCase()+"(\\z|[^a-zA-Z])");
	}
	
	private String addResponse(String s)
	{
		//checks if the key already exists
		if (!others.containsKey(s.substring(s.indexOf("if I say")+10,s.indexOf("then say")-1)))
		{
			//adds the key and response to the other statements list
			others.put(s.substring(s.indexOf("if I say")+10,s.indexOf("then say")-1),s.substring(s.indexOf("then say")+9));
			return "Okay, keywords learned";
		}
		else
			return "Keyword already exists";
	}
	
	private String returnOthers(String s)
	{
		//returns the response to the key
		if (others.containsKey(s))
			return others.get(s);
		return "An error has occured";
	}
	
	private String addToList(String s)
	{
		//checks to see if the list exists and adds it to it
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
		//return the elements of the specififed list if the list is found
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
			//checks to see if the computer can connect to google
			BufferedReader lr = new BufferedReader(new InputStreamReader(
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
		//sets the first letter of the name to uppercase
		name=statement.substring(statement.indexOf("my name is ")+11,
		statement.indexOf("my name is ")+12).toUpperCase();
		//sets every letter after a space, dash, or period to uppercase and everything else to lowercase
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
		//sets the cpu move
		int move=(int)(3*Math.random()+1);
		int yourMove=0;
		String yours=statement;
		//puts the user move into number format
		if (yours.equalsIgnoreCase("rock"))
			yourMove=1;
		else if (yours.equalsIgnoreCase("scissors"))
			yourMove=2;
		else if (yours.equalsIgnoreCase("paper"))
			yourMove=3;
		else
			return "That is not a valid entry, goodbye!";
		//analyzes scenarios
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
		//checks for name and returns good game 
		String n=name!=null?(", "+name+"!"):"!";
		return "Good game"+n;
	}
}