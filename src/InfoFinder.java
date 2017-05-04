import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;

public class InfoFinder
{
	private String line;
	
	public String create(String l)
	{
		String wiki="https://en.wikipedia.org/wiki/";
		line=wiki+l;
		String ans=find();
		if (!ans.substring(0,Math.min(20,ans.length())).equals("An error has occured"))
			return ans;
		line="http://www.ask.com/web?o=0&l=dir&qo=serpSearchTopBox&q="+l;
		String ask=askFind();
		if (ask.substring(0,Math.min(20,ask.length())).equals("An error has occured"))
			return ans;
		return ask;
	}
	
	private String askFind()
	{
		try
		{
			LineNumberReader lr = new LineNumberReader(new InputStreamReader(
			new URL(line).openConnection().getInputStream()));
			String line1=lr.readLine();
			String target="<span class=\"sa_headline\">";
			String lines="";
			while (line1!=null)
			{
				for (int i=0;i<line1.length()-target.length();i++)
					if (line1.substring(i,i+target.length()).equals(target))
					{
						while (line1!=null&&line1.indexOf("<script type=\"text/javascript\">")<0&&line1.indexOf("<br/>")<0)
						{
							lines+=line1;
							line1=lr.readLine();
						}
						return addLine(lines);
					}
				line1=lr.readLine();
			}
			return "An error has occured please be less specfic.";
		}
		catch (IOException e)
		{
			return "An error has occured please be less specfic.";
		}
	}
	
	private String find()
	{
		try
		{
			LineNumberReader lr = new LineNumberReader(new InputStreamReader(
			new URL(line).openConnection().getInputStream()));
			String line1;
			String line2=lr.readLine();
			String line3=lr.readLine();
			String ans;
			while (true)
			{
				line1=line2;
				line2=line3;
				line3=lr.readLine();
				if (line1==null||line2==null||line3==null)
					break;
				else if (line2.equals("</table>")&&line1.equals("</tr>")
				&&line3.substring(0,Math.min(3,line3.length())).equals("<p>"))
				{
					ans=addLine(line3);
					for (int i=0;i<ans.length();i++)
						if (ans.substring(i,Math.min(i+3,ans.length())).equals("â€“"))
						{
							ans=ans.substring(0,i)+"-"+ans.substring(i+3);
							i-=2;
						}
					if (ans.indexOf("may refer to:")>=0)
						return "An error has occured, please be more specific.";
					return ans;
				}
			}
			return "An error has occured, please be less specfic.";
		}
		catch (Exception e)
		{
			return "An error has occured please be less specfic.";
		}
	}
	
	public static String addLine(String line)
	{
		String[] w={"<","(","["};
		String[] w2={">",")","]"};
		for (int i=0;i<w.length;i++)
			line=fix(line,w[i],w2[i]);
		while (line.indexOf("  ")>=0)
			line=line.replaceFirst("  "," ");
		return line.trim();
	}
	
	public static String fix(String a, String q, String q2)
	{
		int pos=0;
		String b="";
		for (int i=0;i<a.length();i++)
		{
			if (a.substring(i,Math.min(a.length(),i+q.length())).equals(q))
			{
				b+=a.substring(pos,i);
				for (int t=i+1;t<a.indexOf(q2,pos);t++)
					if (a.substring(t,Math.min(a.length(),t+q.length())).equals(q))
					{
						pos=a.indexOf(q2,pos)+1;
						break;
					}
				pos=a.indexOf(q2,pos)+q.length();
				i=pos-q.length();
			}
		}
		return b+=a.substring(pos);
	}

}