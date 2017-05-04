import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
public class Calculator
{
public String error="An error has occured";
public String eq;
private String[] otherChar = {"^","x","*","/","+","-"};

public boolean checkEq()
{
	String oper="(\\^|\\+|\\-|\\*|x|\\/)";
	String num="(([0-9]+)(\\.?)([0-9]*))";
	if (Pattern.compile("("+num+"("+oper+num +")*)((=)([a-z]))?").matcher(eq).matches())
		return true;
	return false;
}

	public String equationSolver(String equat)
	{
		if (Pattern.compile("([0-9]+)(\\.?)([0-9]*)").matcher(equat).matches())
			return equat;
		ArrayList<Double> terms=new ArrayList<Double>();
		ArrayList<String> operators=new ArrayList<String>();
		ArrayList<Integer> positions=new ArrayList<Integer>();
		for (int t=0;t<otherChar.length;t++)
		{
			for (int i=0;i<equat.length();i++)
			{
				if (equat.substring(i,i+1).equals(otherChar[t]))
					positions.add(i);
			}
		}
		int endPos=0;
		int startPos=-1;
		Collections.sort(positions);
		for (int i=0;i<positions.size();i++)
		{
			endPos=positions.get(i);
			terms.add(cStr(equat.substring(startPos+1,endPos)));
			operators.add(equat.substring(endPos,endPos+1));
			startPos=endPos;
		}
		if (equat.contains("="))
			terms.add(cStr(equat.substring(endPos+1,equat.indexOf("="))));
		else
			terms.add(cStr(equat.substring(endPos+1)));
		for (int i=0;i<otherChar.length;i++)
			for (int t=0;t<operators.size();t++)
				if (otherChar[i].equals(operators.get(t)))
					{
						terms.set(t,twoTerms(operators.get(t),terms.get(t),terms.get(t+1)));
						terms.remove(t+1);
						operators.remove(t);
						t--;
					}
		double ans=terms.get(0);
		return changeFormat(cDub(ans));
	}

	public String changeFormat(String ans)
	{
		DecimalFormat intFormat = new DecimalFormat("#");
		String fin=ans;
		if (fin.equals("Infinity"))
			fin=error;
		else if (cStr(ans)==Math.ceil(cStr(ans)))
			fin=intFormat.format(cStr(ans));
		return fin;
	}

	public double twoTerms(String type, double term1, double term2)
	{
		double ans=0;
		if (type.equals("^"))
			ans=Math.pow(term1,term2);
		else if (type.equals("*")||type.equals("x"))
			ans=term1*term2;
		else if (type.equals("/"))
			ans=term1/term2;
		else if (type.equals("+"))
			ans=term1+term2;
		else if (type.equals("-"))
			ans=term1-term2;
		return ans;
	}

	public double cStr(String a)
	{
		return Double.parseDouble(a);
	}

	public String cDub(Double a)
	{
		return String.valueOf(a);
	}
}