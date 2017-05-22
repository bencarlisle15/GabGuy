//created by Andrew Wolstenholme with additional support from Ben Carlisle who takes no credit for the code quality
import java.awt.Color;		//just imported things when error messages said I need to
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class BrickBreaker extends JPanel implements KeyListener, MouseListener,Runnable {

	private int a=0; 									//will be uses as position of paddle
	private double xPos =(600* Math.random()+1);		//starts at any point across
	private double yPos =(150* Math.random()+51);	//starts below blocks so they dont accidentally get hit
	private double xSpeed = 0;			//pos = v*t or speed added to current pos. since updated by frame, there is smooth movement
	private double ySpeed = 0;
	private String message;
	private int messagePos;
	private Color brick;
	private Color  ball;
	private Color background;
	private boolean first=true;
	private boolean[] redBlock;
	private boolean gameOver=false;	//array which tells if a block is there or not
	
	
	
	
	
	
	
	
	
	
	public BrickBreaker(){			//constuctor creates label to later be added to the panel as well as initializing the redBlock array and setting them all to true 
		addKeyListener(this);
		redBlock = new boolean[6];
		for(int k = 0; k<redBlock.length; k++)
			redBlock[k]=true;
		message = "Press the Space Bar to play";
		brick = new Color(200,60,50);					//custom colors (red,green,blue)
		background = new Color(50,50,100);
		ball = new Color(50,250,50);
		messagePos=100;}

	

	
	
	
	
	
	
	
	public void paintBlocks(Graphics g){					//paints a block of length 50 every 100 units if red block says a block exists there. makes them red
		int blockNum = 0;
		for(int i = 0; i<=getWidth();i+=100){
			if(redBlock[blockNum]){
				g.setColor(brick);
				g.fillRect(i, 0, 50, 50);}
			blockNum++;							//move through boolean array
			g.setColor(Color.white);}}			//sets back to black to paint ball

	
	
	
	
	
	
	
	
	
	
	private void moveBall(){ 											//Many many if statements
		if (xPos < 0)													//bounces off wall reverses x direction. tests first since if true doesnt matter to any other if statement
			xSpeed = -xSpeed;
	
		else if (xPos + xSpeed > getWidth() - 10)					
			xSpeed = -xSpeed;
		
		else if (yPos < 0)													//if the ball hits the top of the panel bounces back downward. no blocks are removed
			ySpeed =-ySpeed;
		
		else if (yPos <50){	
			for(int i=0;i<redBlock.length;i++){								//if hits on top, makes redblock at the space false. next frame wont have a painted cube there
				if(ySpeed<0&&xPos > (i*100-10) && xPos< ((i*100) + 50) && redBlock[i]){						//this set tests for the tops of blocks being hit
					redBlock[i]=false;
					ySpeed=-ySpeed;}}
			for (int i=0;i<redBlock.length;i++)
				if (redBlock[i]&&(xSpeed>0&&xPos>=i*100-5&&xPos<=i*100+5||xSpeed<0&&xPos>=i*100+45&&xPos<=i*100+55)){
					xSpeed = -xSpeed;
					redBlock[i]=false;}
			repaint();}								//reverses direction and removes block
																	// repaint if a block is hit to get rid of it
			
		
		else if (yPos + ySpeed > getHeight() - 20 && (a < xPos  && a+50 > xPos) && ySpeed>0)
		{
		ySpeed =-ySpeed;//speedVectorxSpeed *=0.9* Math.pow(Math.sin(135),2);
		if (xSpeed>0)
			xSpeed-=0.3;
		else
			xSpeed+=0.3;//speedVector* Math.pow(Math.cos(135),2);

		}
	
	else if (yPos + ySpeed > getHeight() - 20 && (a+51 < xPos  && a+150 > xPos) && ySpeed>0)
		{
		ySpeed =-ySpeed;
		}
	
	else if (yPos + ySpeed > getHeight() - 20 && (a+151 < xPos  && a+200 > xPos) && ySpeed>0)
		{
		ySpeed =-ySpeed;
		if (xSpeed>0)
			xSpeed+=0.3;
		else
			xSpeed-=0.3;
		}
		
		
		else if (yPos > getHeight()-10){			//if the ball hits the bottom, change printed string to game over and stop movement
			xSpeed =0;
			ySpeed=0;
			gameOver=true;
			message = "Game Over...";}
		
		xPos += xSpeed;			//increase the postion always for every frame
		yPos += ySpeed;
	}

	
	
	
	
	
	
	
	
	
	public void paintBall(Graphics g){							//paints only the ball on the graphics object sent from paint to the color created in constructor
		g.setColor(ball);
		setBackground(background);								//paints background color set in constructor
		g.fillOval((int)(xPos),(int)(yPos), 10, 10);}						//creates a ball of diameter 10. (xPos,yPos) is the top left corner
	
	
	
	
	
	
	
	
	
	
	public void paint(Graphics g){				//extended paint method from graphics. calls the super constructor then calls for ball and blocks to be painted.
		super.paint(g);							
		paintBall(g);
		paintBlocks(g);
		g.fillRect(a, getHeight()-10, 200, 10);					//paints paddle and makes position a which can be changed later
		boolean didWin = true;									//creates a way to know if all blocks are gone
		for (int p=0; p<redBlock.length; p++){					//since paint is called each frame, this tests for when all the blocks are gone and then will call win method
			if(redBlock[p])										
				didWin=false;}		
		if (didWin){
			message = "You Win!";								//changes label and stops padle movement
			gameOver=true;}
		Font font = g.getFont().deriveFont(30f);				//makes a font object which allows font to be set to a float
	    g.setFont(font);		
	    g.drawString(message, messagePos, getHeight()/2);
	    if (gameOver){
	    	g.drawString("Click Anywhere to Close",messagePos-50,getHeight()/2+50);}}		//puts the message in the middle of the screen


	

	
	
	
	
	
	
	public void create(BrickBreaker game){					//creates the whole frame where game can be played. makes it a specified size which cant be changed. Sets to visible and add a way to take user input
		JFrame frame = new JFrame("Break the Bricks");
		try
		{
			frame.setIconImage(ImageIO.read(getClass().getResource("files/brickbreaker.png")));
		}
		catch (IOException e)
		{
		}
		frame.getContentPane().add(game);
		frame.pack();
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		//Allows to close
		frame.setResizable(false);
		frame.addKeyListener(game);
		frame.addMouseListener(game);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);}







	public void run(){
		while (true){					//true is always true so ball can move and repaints every frame. also can never end game without exiting panel
			moveBall();
			repaint();
			try{
				Thread.sleep(2);}			//this slows the speed
			catch (InterruptedException e){
				e.printStackTrace();}}}
	
	
	
	
	
	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}			//from implemented keyListener but only need one way of reading keys. Key pressed is my desired movement method
	@Override
	public void keyReleased(KeyEvent e) {}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void keyPressed(KeyEvent e){				//a press of the arrow moves a, the position of bar, 50 units in said direction
		if (!gameOver&&e.getKeyCode()==KeyEvent.VK_LEFT)
			a=Math.max(a-75, 0);						//greater of 0 or 50 less than a. only 0 if at end
		else if (!gameOver&&e.getKeyCode()==KeyEvent.VK_RIGHT)
			a=Math.min(a+75, getWidth()-200);			//a position is set to increase by 50 unless at end through math.min
		else if (e.getKeyCode()==KeyEvent.VK_SPACE&&first){
			xSpeed = 1;
			ySpeed = 1;
			message = "Break The Bricks";
			messagePos= 175;
			first=false;}}










	public void nextLevel(){
		
	}










	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		if (gameOver)
			((Window) SwingUtilities.getRoot(this)).dispose();
	}










	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}










	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}










	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}










	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}