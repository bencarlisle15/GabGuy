import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TicTacToe extends JPanel implements MouseListener
{
	//private instance variables
	private static final long serialVersionUID = 1L;
	private int width;
	private int padding;
	private int height;
	private int space;
	private int round;
	private int oneStart;
	private int twoStart;
	private int threeStart;
	private int oneEnd;
	private int twoEnd;
	private int threeEnd;
	private int ovalX;
	private int ovalY;
	private int ovalWidth;
	private int ovalHeight;
	private int lineOneX1;
	private int lineOneY1;
	private int lineOneX2;
	private int lineOneY2;
	private int lineTwoX1;
	private int lineTwoY1;
	private int lineTwoX2;
	private int lineTwoY2;
	private int finalR1=0;
	private int finalC1=0;
	private int finalR2=0;
	private int finalC2=0;
	private boolean win;
	private boolean draw;
	private boolean done;
	private boolean gameFinished=false;
	private boolean first=true;
	private final char[][] board=new char[3][3];
	
	public TicTacToe()
	{
		//sets Background, layout and listener
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g)
	{
		//paints every letter if it exists
		super.paintComponent(g);
		paintOrginal(g);
		for (int r=0;r<board.length;r++)
			for (int c=0;c<board[r].length;c++)
				if (board[r][c]!=0)
					paintLetter(g,r,c);
		//if the game is over a line must identify the win path
		if (gameFinished)
			paintLine(g);
	}
	
	private void paintLine(Graphics g)
	{
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(Color.YELLOW);
		//makes a rounded line of width fifteen from and to the specified positions
		g2.setStroke(new BasicStroke(15,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2.drawLine(finalR1,finalC1,finalR2,finalC2);
		//calls the game is over method
		gameIsOver();
	}
	
	private void paintLetter(Graphics g,int r,int c)
	{
		//adds width of the row or col
		int addRow=0;
		int addCol=0;
		if (c==1)
			addRow=twoStart-5*padding/4;
		else if (c==2)
			addRow=threeStart-5*padding/4;
		if (r==1)
			addCol=twoStart-5*padding/4;
		else if (r==2)
			addCol=threeStart-5*padding/4;
		//draws an o
		if ('O'==board[r][c])
		{
			Graphics2D g2=(Graphics2D) g;
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(10));
			g2.drawOval(ovalX+addRow,ovalY+addCol,ovalWidth,ovalHeight);
		}
		//draws an x
		else if ('X'==board[r][c])
		{
			Graphics2D g2=(Graphics2D) g;
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(10,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			g2.drawLine(lineOneX1+addRow,lineOneY1+addCol,lineOneX2+addRow,lineOneY2+addCol);
			g2.drawLine(lineTwoX1+addRow,lineTwoY1+addCol,lineTwoX2+addRow,lineTwoY2+addCol);
		}
	}
	
	public void paintOrginal(Graphics g)
	{
		//sets instance variables if its the first time
		if (first)
		{
			width=getWidth()/20;
			padding=getHeight()/20;
			height=getHeight()-2*padding;
			space=(height-2*width)/3;
			round=getWidth()/20;
			oneStart=padding;
			twoStart=space+padding+width;
			threeStart=2*space+padding+2*width;
			oneEnd=space+padding;
			twoEnd=2*space+padding+width;
			threeEnd=3*space+padding+2*width;
			ovalX=2*padding;
			ovalY=2*padding;
			ovalWidth=space-3*padding/2;
			ovalHeight=space-3*padding/2;
			lineOneX1=2*padding;
			lineOneY1=2*padding;
			lineOneX2=space+padding/2;
			lineOneY2=space+padding/2;
			lineTwoX1=space+padding/2;
			lineTwoY1=2*padding;
			lineTwoX2=2*padding;
			lineTwoY2=space+padding/2;
			first=false;
		}
		//draws the four lines to create a grid
		g.setColor(Color.RED);
		g.fillRoundRect(padding+space,padding,width,height,round,round);
		g.fillRoundRect(padding+2*space+width,padding,width,height,round,round);
		g.fillRoundRect(padding,padding+space,height,width,round,round);
		g.fillRoundRect(padding,padding+2*space+width,height,width,round,round);
	}
	public void create(TicTacToe t)
	{
		//creates a new frame and adds the tic tac toe content to it
		JFrame frame=new JFrame("Tic Tac Toe");
		frame.getContentPane().add(t);
		//sets the icon to an x
		try
		{
			frame.setIconImage(ImageIO.read(getClass().getResource("files/x.png")));
		}
		catch (IOException e)
		{
		}
		//disposes on close
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//sets size and location to middle and then visible
		frame.pack();
		frame.setSize(500,500);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		//kills the game on click after it is over
		if (gameFinished)
			((Window) SwingUtilities.getRoot(this)).dispose();
		//click in dimensions for each square sets the square in the 2d array to x and goes to the comp turn
		else if (e.getX()>=oneStart&&e.getX()<oneEnd&&e.getY()>=oneStart&&e.getY()<oneEnd&&board[0][0]==0)
		{
			board[0][0]='X';
			nextMove();
		}
		else if (e.getX()>=twoStart&&e.getX()<twoEnd&&e.getY()>=oneStart&&e.getY()<oneEnd&&board[0][1]==0)
		{
			board[0][1]='X';
			nextMove();
		}
		else if (e.getX()>=threeStart&&e.getX()<threeEnd&&e.getY()>=oneStart&&e.getY()<oneEnd&&board[0][2]==0)
		{
			System.out.println("box3");
			board[0][2]='X';
			nextMove();
		}
		else if (e.getX()>=oneStart&&e.getX()<oneEnd&&e.getY()>=twoStart&&e.getY()<twoEnd&&board[1][0]==0)
		{
			board[1][0]='X';
			nextMove();
		}
		else if (e.getX()>=twoStart&&e.getX()<twoEnd&&e.getY()>=twoStart&&e.getY()<twoEnd&&board[1][1]==0)
		{
			board[1][1]='X';
			nextMove();
		}
		else if (e.getX()>=threeStart&&e.getX()<threeEnd&&e.getY()>=twoStart&&e.getY()<twoEnd&&board[1][2]==0)
		{
			board[1][2]='X';
			nextMove();
		}
		else if (e.getX()>=oneStart&&e.getX()<oneEnd&&e.getY()>=threeStart&&e.getY()<threeEnd&&board[2][0]==0)
		{
			board[2][0]='X';
			nextMove();
		}
		else if (e.getX()>=twoStart&&e.getX()<twoEnd&&e.getY()>=threeStart&&e.getY()<threeEnd&&board[2][1]==0)
		{
			board[2][1]='X';
			nextMove();
		}
		else if (e.getX()>=threeStart&&e.getX()<threeEnd&&e.getY()>=threeStart&&e.getY()<threeEnd&&board[2][2]==0)
		{
			board[2][2]='X';
			nextMove();
		}
	}
	
	//methods only used to implement mouselistener
	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}
	
	//checks to see if a win of either side is present
	private void checkWin()
	{
		//types of win scenarios and send the positions of the first and last points of the row
		for (int i=0;i<board.length;i++)
			if (board[i][0]==board[i][1]&&board[i][2]==board[i][0]&&board[i][0]!=0)
				gameOver(i,0,i,2);
		for (int i=0;i<board.length;i++)
			if (board[0][i]==board[1][i]&&board[2][i]==board[0][i]&&board[0][i]!=0)
				gameOver(0,i,2,i);
		if (board[0][0]==board[1][1]&&board[2][2]==board[0][0]&&board[0][0]!=0)
			gameOver(0,0,2,2);
		if (board[0][2]==board[1][1]&&board[2][0]==board[0][2]&&board[0][2]!=0)
			gameOver(0,2,2,0);
	}
	
	private void gameOver(int r1, int c1, int r2, int c2)
	{
		//identifies who won and where the coordinates of the first and last points are and that the game is over
		done=true;
		if (board[r1][c1]=='X')
			win=true;
		if (c1==0)
			finalR1=oneStart+space/2+5;
		else if (c1==1)
			finalR1=getHeight()/2;
		else if (c1==2)
			finalR1=threeEnd-space/2;
		if (r1==0)
			finalC1=oneStart+space/2+5;
		else if (r1==1)
			finalC1=getWidth()/2;
		else if (r1==2)
			finalC1=threeEnd-space/2;
		if (c2==0)
			finalR2=oneStart+space/2+5;
		else if (c2==1)
			finalR2=getHeight()/2;
		else if (c2==2)
			finalR2=threeEnd-space/2;
		if (r2==0)
			finalC2=oneStart+space/2+5;
		else if (r2==1)
			finalC2=getWidth()/2;
		else if (r2==2)
			finalC2=threeEnd-space/2;
		gameFinished=true;
		//repaints which will now paint the new line
		repaint();
	}
	
	//cpu move
	private void nextMove()
	{
		//makes sure that there are less os than xs so that cpu cannot make two moves
		int x=0;
		int o=0;
		for (int r=0;r<board.length;r++)
			for (int c=0;c<board[r].length;c++)
			{
				if (board[r][c]=='X')
					x++;
				else if (board[r][c]=='O')
					o++;
			}
		if (o>=x||done)
			return;
		//checks to see if a win or draw is present
		checkWin();
		if (fullBoard())
		{
			//draw is present and calls the gameIsOver method
			draw=true;
			gameIsOver();
			return;
		}
		//cpu move to block most user two in a rows
		for (int r=0;r<2&&!done;r++)
		{
			if (board[r][2]==0&&board[r][0]==board[r][1]&&board[r][0]=='X')
			{
				board[r][2]='O';
				done=true;
			}
			else if (board[r][0]==0&&board[r][2]==board[r][1]&&board[r][2]=='X')
			{
				board[r][0]='O';
				done=true;
			}
		}
		for (int c=0;c<2&&!done;c++)
		{
			if (board[2][c]==0&&board[0][c]==board[1][c]&&board[0][c]=='X')
			{
				board[2][c]='O';
				done=true;
			}
			else if (board[0][c]==0&&board[2][c]==board[1][c]&&board[2][c]=='X')
			{
				board[0][c]='O';
				done=true;
			}
		}
		//if no two in a rows are present then pick one of the five optimal positions
		if (!done)
		{
			if (board[2][2]==0&&board[0][0]==board[1][1]&&board[0][0]=='X')
			{
				board[2][2]='O';
				done=true;
			}
			else if (board[0][2]==0&&board[2][0]==board[1][1]&&board[2][0]=='X')
			{
				board[0][2]='O';
				done=true;
			}
		}
		for (int i=0;i<2&&!done;i++)
		{
			int rand=(int)(Math.random()*3);
			if (board[rand][rand]==0)
			{
				board[rand][rand]='O';
				done=true;
			}
		}
		//picks a random position
		int rand1;
		int rand2;
		while (!done)
		{
			rand1=(int)(Math.random()*3);
			rand2=(int)(Math.random()*3);
			if (board[rand1][rand2]==0)
			{
				board[rand1][rand2]='O';
				done=true;
			}
		}
		//checks to see if win or draw status has changed and repaints to add the new char
		checkWin();
		repaint();
		if (fullBoard())
		{
			draw=true;
			gameIsOver();
			return;
		}
		//resets the done status
		done=false;
	}
	
	private void gameIsOver()
	{
		//sets gameFinished status and identifies winner in a textpane
		gameFinished=true;
		String ans;
		if (win)
			ans="Congratulations! You Win!";
		else if (draw)
			ans="It's a draw.";
		else
			ans="That's a win for me.";
		JTextPane status=new JTextPane();
		status.setText(ans+"\nClick Anywhere to Exit");
		//sets status in the top middle of the frame and as nonfocusable or editable with a mouselistener background 
		SimpleAttributeSet center=new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		status.getStyledDocument().setParagraphAttributes(0,status.getStyledDocument().getLength(), center, false);
		status.setFocusable(false);
		status.setEditable(false);
		status.setBackground(Color.CYAN);
		status.setFont(status.getFont().deriveFont(24f));
		status.addMouseListener(this);
		status.setSize(getWidth(),(int)(status.getPreferredSize().getHeight()));
		//added to the frame and repainted
		add(status,BorderLayout.NORTH);
		repaint();
	}

	private boolean fullBoard()
	{
		//checks to see if every spot if filled
		for (int r=0;r<board.length;r++)
			for (int c=0;c<board[r].length;c++)
				if (board[r][c]==0)
					return false;
		return true;
	}
}