import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

public class OutputAdd extends Thread
{
	//private instance variables
	private Magpie mag;
	private StyledDocument out;
	private SimpleAttributeSet left;
	private boolean checking;
	private Main main;
	private Thread thread;
	private JTextPane output;
	private String ans;
	
	public OutputAdd(Magpie ma, StyledDocument o, SimpleAttributeSet l, Main m, Thread t,JTextPane ou)
	{
		mag=ma;
		out=o;
		left=l;
		main=m;
		thread=t;
		output=ou;
	}
	
	//overrides run method
	public void run()
	{
		//synchronizes this thread with the variable thread
		synchronized(thread)
		{
			//constantly performing checkmag
			while (true)
				checkMag();
		}
	}
	
	public boolean getChecking()
	{
		//returns checking status
		return checking;
	}
	
	public void setChecking(boolean b)
	{
		//sets the checking status
		checking=b;
	}
	
	private void checkMag()
	{
		//adds a minute pause to the program since it goes to fast without it
		try
		{
			Thread.sleep(0);
		}
		catch (InterruptedException e1)
		{
		}
		//only continues if it is supposed to check
		if (checking)
		{
			//gets the response initially since getResponse makes the response null afterwards
			ans=mag.getResponse();
			//if an answer has been reached
			if (ans!=null)
			{
				//remove the working message and add the response aligned left
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
				//sets the temp to null and stops checking
				main.setTemp(null);
				checking=false;
			}
		}
	}
}
