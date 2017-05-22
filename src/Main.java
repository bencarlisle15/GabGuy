import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Main extends JPanel implements KeyListener, FocusListener
{
	//private instance variables
	//JPanels like to have serialVerisionUIDs even if they are default
	private static final long serialVersionUID=1L;
	private static JTextPane output=new JTextPane();
	private static JTextField input=new JTextField();
	private static JScrollPane scrollPane=new JScrollPane(output);
	private StyledDocument out=output.getStyledDocument();
	private SimpleAttributeSet left=new SimpleAttributeSet();
	private SimpleAttributeSet right=new SimpleAttributeSet();
	private BorderLayout bl=new BorderLayout();
	private Magpie mag=new Magpie();
	private static boolean userError=false;
	private static boolean help=false;
	private int pos;
	private ArrayList<String> inputResponses=new ArrayList<String>();
	private String temp=null;
	private OutputAdd outAdd=new OutputAdd(mag, out, left,this,Thread.currentThread(),output);
	private ArrayList<Thread> threads=new ArrayList<Thread>();

	public Main()
	{
		//"initializes" the two style attributes
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		//sets output to focusable and non editable
		output.setEditable(false);
		output.setFocusable(true);
		//original text set to left alignment
		try
		{
				out.setParagraphAttributes(out.getLength(), 1, left, false);
				out.insertString(out.getLength(),"Hi I'm GabGuy, let's talk\nHit F1 for help.\n", left);
		}
		catch (BadLocationException e)
		{
		}
		//adds first and last response to the responses ArrayList
		inputResponses.add("");
		inputResponses.add("");
		//default input text
		input.setText("Enter text here");
		//adds key and focus listeners to input
		input.addFocusListener(this);
		input.addKeyListener(this);
		//sets a gap between elements in the layout and set it as the layout
		bl.setVgap(5);
		setLayout(bl);
		//adds a border
		setBorder(new EmptyBorder(5,5,5,5));
		//default size
		setPreferredSize(new Dimension(400,400));
		//adds elements
		add(scrollPane,BorderLayout.CENTER);
		add(input,BorderLayout.SOUTH);
	}

	public void create(Main m)
	{
		//initializes JFrame with name
		JFrame frame=new JFrame("GabGuy");
		//adds icon
		try
		{
			frame.setIconImage(ImageIO.read(getClass().getResource("files/chat.png")));
		}
		catch (IOException e)
		{
		}
		//default close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//adds content
		frame.getContentPane().add(m);
		//makes frame correct size
		frame.pack();
		//puts frame in middle
		frame.setLocationRelativeTo(null);
		//makes it visible
		frame.setVisible(true);
		//starts responses checking thread
		outAdd.start();
	}
	
	public static void main(String[] args)
	{
		//performs actions
		Main m=new Main();
		m.create(m);
		//starts focused on input
		input.requestFocusInWindow();
	}
	
	private void enterString()
	{
		//creates and runs a new magpie thread
		threads.add(new Thread(mag));
		threads.get(threads.size()-1).start();
		//sets checking status to true
		outAdd.setChecking(true);
		//adds working message
		try
		{
			out.setParagraphAttributes(out.getLength(), 1, left, false);
			out.insertString(out.getLength(),"working...", left);
		}
		catch (BadLocationException e)
		{
		}
	}
	
	public void setTemp(String t)
	{
		//sets the temp
		temp=t;
	}

	public void keyPressed(KeyEvent e)
	{
		//if their is text and the user hits enter
		if (e.getKeyCode()==KeyEvent.VK_ENTER&&!input.getText().equals(""))
		{
			//removes spaces and sets it to the temp
			temp=input.getText().trim();
			//goes to the setText method to add the text to the textpane
			setText();
			//insures that help is not needed
			help=false;
		}
		//adds bash style commands for up and down
		else if (e.getKeyCode()==KeyEvent.VK_UP)
		{
			//sets the input text to the (if available) the response below (visually above) the current pos
			pos=Math.max(0,pos-1);
			if (inputResponses.size()>pos&&pos>=0)
				input.setText(inputResponses.get(pos));
		}
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			//sets the input text to the (if available) the response above (viually below) the current pos
			pos=Math.min(inputResponses.size()-1,pos+1);
			if (inputResponses.size()>pos&&pos>=0)
				input.setText(inputResponses.get(pos));
		}
		//scrolls the textarea to the top of the area
		else if (e.getKeyCode()==KeyEvent.VK_PAGE_UP)
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
		//scrolls the textarea to the bottom of the area
		else if (e.getKeyCode()==KeyEvent.VK_PAGE_DOWN)
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	@SuppressWarnings("deprecation")
	public void keyReleased(KeyEvent e)
	{
		//if checking is occuring
		if (outAdd.getChecking())
		{
			//stops the most recent thread
			//deprecated methods still work fine and can be necessary
			threads.get(threads.size()-1).stop();
			//stops checking
			outAdd.setChecking(false);
			//removes the working message
			try
			{
				out.remove(out.getText(0,out.getLength()).lastIndexOf("working..."),10);
			}
			catch (BadLocationException e1)
			{
			}
			//goes to the help method if desired
			if (!help&&e.getKeyCode()==KeyEvent.VK_F1)
				help();
			else
			{
				//tells the user that their process has stopped and removes the working message
				try
				{
					out.remove(out.getText(0,out.getLength()).lastIndexOf("working..."),10);
				}
				catch (BadLocationException e1)
				{
				}
				try
				{
					out.setParagraphAttributes(out.getLength(), 1, left, false);
					out.insertString(out.getLength(),"Process stopped"+"\n", left);
				}
				catch (BadLocationException f)
				{
				}
				input.setText("");
			}
		}
		else
		{
			//if the user hit enter and temp has a value
			if (!userError&&e.getKeyCode()==KeyEvent.VK_ENTER&&temp!=null)
			{
				//go to the enterString method
				enterString();
				//give input focus and scrolls to the bottom of the textarea
				input.requestFocusInWindow();
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			}
			//goes to the help method if desired
			else if (!help&&e.getKeyCode()==KeyEvent.VK_F1)
				help();
		}
	}
	
	//required to implement KeyListener
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void focusGained(FocusEvent arg0)
	{
		//sets the text to nothing if it is the default or max text response
		if (input.getText().equals("Enter text here")||
		input.getText().equals("Words larger than 50 characters are not real and not allowed"))
			input.setText("");
		userError=false;
	}

	public void focusLost(FocusEvent arg0)
	{
		//sets the text to the default if its nothing
		if (input.getText().equals(""))
			input.setText("Enter text here");
	}
	
	public void help()
	{
		//sets the code to 0 regardless of what it is
		mag.setCode(0);
		//makes the temp "help" and sets it to the statement
		temp="help";
		mag.setStatement(temp);
		//adds the response and sets the pos to the most recent response
		pos=inputResponses.size();
		inputResponses.add(inputResponses.size()-1,temp);
		//sets text to nothing identifies there is no error and that help is needed
		input.setText("");
		userError=false;
		help=true;
		//calls the enterString method
		enterString();
	}
	
	private void setText()
	{
		int count=0;
		//checks to make sure no word is over 50 characters to prevent spam
		//done since it might add an ugly and obstructive horozontal scroll bar 
		for (int i=0;i<temp.length();i++)
		{
			count++;
			if (temp.charAt(i)==' ')
				count=0;
			else if (count>50)
			{
				input.setText("Words larger than 50 characters are not real and not allowed");
				//sets the userError equal to true and removes focus from the input
				userError=true;
				temp=null;
				output.requestFocusInWindow();
				return;
			}
		}
		//makes sure no errors are occurring
		if (!userError)
		{
			//sets the statement to temp and its it the response ArrayList
			mag.setStatement(temp);
			pos=inputResponses.size();
			inputResponses.add(inputResponses.size()-1,temp);
			input.setText("");
			//adds temp to the textarea aligned to the right
			try
			{
				out.setParagraphAttributes(out.getLength(), 1, right, false);
				out.insertString(out.getLength(),temp+"\n", right);
			}
			catch (BadLocationException e1)
			{
			}
		}
		else
			//the time for userErrors is over
			userError=false;
	}
}