import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

public class OutputAdd extends Thread
{
	private Magpie mag;
	private StyledDocument out;
	private SimpleAttributeSet left;
	public boolean checking;
	private Main main;
	private Thread thread;
	private JTextPane output;
	public String ans;
	
	public OutputAdd(Magpie ma, StyledDocument o, SimpleAttributeSet l, Main m, Thread t,JTextPane ou)
	{
		mag=ma;
		out=o;
		left=l;
		main=m;
		thread=t;
		output=ou;
	}
	
	public void run()
	{
		synchronized(thread)
		{
			while (true)
				checkMag();
		}
	}
	
	public void checkMag()
	{
		try
		{
			Thread.sleep(0);
		}
		catch (InterruptedException e1)
		{
		}
		if (checking)
		{
			ans=mag.getResponse();
			if (ans!=null)
			{
				try
				{
					out.remove(out.getText(0,out.getLength()).lastIndexOf("working..."),10);
					out.setParagraphAttributes(out.getLength(), 1, left, false);
					out.insertString(out.getLength(),ans+"\n", left);
					output.setCaretPosition(output.getDocument().getLength());
				}
				catch (BadLocationException e)
				{
				}
				main.temp=null;
				checking=false;
			}
		}
	}
}
