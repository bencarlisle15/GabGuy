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
	private static final long serialVersionUID=1L;
	private static JTextPane output=new JTextPane();
	private static JTextField input=new JTextField();
	private static JScrollPane scrollPane=new JScrollPane(output);
	private static boolean first=true;
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
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		output.setEditable(false);
		output.setFocusable(true);
		if (first)
		{
			try
			{
					out.setParagraphAttributes(out.getLength(), 1, left, false);
					out.insertString(out.getLength(),"Hi I'm GabGuy, let's talk\nHit F1 for help.\n", left);
			}
			catch (BadLocationException e)
			{
			}
			inputResponses.add("");
		}
		first=false;
		input.setText("Enter text here");
		input.addFocusListener(this);
		input.addKeyListener(this);
		bl.setVgap(5);
		setLayout(bl);
		setBorder(new EmptyBorder(5,5,5,5));
		setPreferredSize(new Dimension(400,400));
		add(scrollPane,BorderLayout.CENTER);
		add(input,BorderLayout.SOUTH);
	}

	public void create()
	{
		JFrame frame=new JFrame();
		try
		{
			frame.setIconImage(ImageIO.read(getClass().getResource("files/chat.png")));
		}
		catch (IOException e)
		{
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Main());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setTitle("GabGuy");
		frame.setVisible(true);
		outAdd.start();
	}
	
	public static void main(String[] args)
	{	
		new Main().create();
		input.requestFocusInWindow();	
	}
	
	private void enterString()
	{
		threads.add(new Thread(mag));
		threads.get(threads.size()-1).start();
		outAdd.setChecking(true);
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
		temp=t;
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_ENTER&&!input.getText().equals(""))
		{
			temp=input.getText().trim();
			setText();
			help=false;
		}
		else if (e.getKeyCode()==KeyEvent.VK_UP)
		{
			pos=Math.max(0,pos-1);
			if (inputResponses.size()>pos&&pos>=0)
				input.setText(inputResponses.get(pos));
		}
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			pos=Math.min(inputResponses.size()-1,pos+1);
			if (inputResponses.size()>pos&&pos>=0)
				input.setText(inputResponses.get(pos));
		}
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+5);
		else if (e.getKeyCode()==KeyEvent.VK_PAGE_UP)
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
		else if (e.getKeyCode()==KeyEvent.VK_PAGE_DOWN)
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	@SuppressWarnings("deprecation")
	public void keyReleased(KeyEvent e)
	{
		if (outAdd.getChecking())
		{
			threads.get(threads.size()-1).stop();
			outAdd.setChecking(false);
			try
			{
				out.remove(out.getText(0,out.getLength()).lastIndexOf("working..."),10);
				out.setParagraphAttributes(out.getLength(), 1, left, false);
				out.insertString(out.getLength(),"Process stopped"+"\n", left);
			}
			catch (BadLocationException f)
			{
			}
			input.setText("");
		}
		else
		{
			if (!userError&&e.getKeyCode()==KeyEvent.VK_ENTER&&temp!=null)
			{
				enterString();
				input.requestFocusInWindow();
				output.setCaretPosition(output.getDocument().getLength());
			}
			else if (!help&&e.getKeyCode()==KeyEvent.VK_F1)
				try
				{
					out.setParagraphAttributes(out.getLength(), 1, left, false);
					out.insertString(out.getLength(),mag.getResponse("help")+"\n", left);
					help=true;
					input.requestFocusInWindow();
				}
				catch (BadLocationException f)
				{
				}
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}
	
	public void focusGained(FocusEvent arg0)
	{
		if (input.getText().equals("Enter text here")||
		input.getText().equals("Words larger than 50 characters are not real and not allowed"))
			input.setText("");
	}

	public void focusLost(FocusEvent arg0)
	{
		if (input.getText().equals(""))
			input.setText("Enter text here");
	}
	
	private void setText()
	{
		int count=0;
		for (int i=0;i<temp.length();i++)
		{
			count++;
			if (temp.charAt(i)==' ')
				count=0;
			else if (count>50)
			{
				input.setText("Words larger than 50 characters are not real and not allowed");
				userError=true;
				temp=null;
				output.requestFocusInWindow();
				return;
			}
		}
		if (!userError)
		{
			mag.setStatement(temp);
			pos=inputResponses.size();
			inputResponses.add(inputResponses.size()-1,temp);
			input.setText("");
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
			userError=false;
	}
}