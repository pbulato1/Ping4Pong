package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.sampled.LineUnavailableException;

import framework.RandomNumberGenerator;
import main.Game;
import main.GameMain;
import main.Resources;
import model.AI2Paddle;
import model.AI3Paddle;
import model.AIPaddle;
import model.Ball;
import model.Paddle;
import model.PowerUp;
//382
public class PlayState extends State{
	
	 public static AI3Paddle paddleBottom; 
	 public static AIPaddle paddleRight;
	 public static AI2Paddle paddleTop;
	 public static Paddle paddleLeft;
	 public static Ball ball;
	 public static PowerUp powerUp;
	 
	 private static final int PADDLE_WIDTH = 15;
	 private static final int PADDLE_HEIGHT = 60;
	 private static final int BALL_DIAMETER = 20;
	 
	 public static int player1Score,player2Score,player3Score,player4Score,rand,longestRally,currentRally,totalHits,totalPoints,deaths,random;
	 public static boolean change,oneAlive,twoAlive,threeAlive,fourAlive,isGameOver,already,started,forced,easy,medium,hard,red,powerUps,soundFX,musicOn,gamePlayMusic,pos;
     private String path;
    
	Resources player = new Resources();
	Font scoreFont;
	Font statsFont;
	Font goFont;
	private int i,j,k,p;
	private int powerOnLeft;
	private int powerOnRight;
	private int powerOnTop;
	private int powerOnBottom;
	private int flashLeft;
	private int flashRight;
	private int flashTop;
	private int flashBottom;
	
	 @Override
	public void init() {
		 
		paddleLeft = new Paddle(0, 195, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddleRight = new AIPaddle(785, 195, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddleTop = new AI2Paddle(370,0,PADDLE_HEIGHT, PADDLE_WIDTH);
		paddleBottom = new AI3Paddle(370,435,PADDLE_HEIGHT, PADDLE_WIDTH);
		ball = new Ball(300, 200, BALL_DIAMETER, BALL_DIAMETER);
		powerUp = new PowerUp(370,225,30,30);
		scoreFont = new Font("SansSerif", Font.BOLD, 25);
		goFont = new Font("SansSerif", Font.BOLD, 70);
		statsFont = new Font("SansSerif", Font.BOLD, 15);
	
		player1Score = 5;
		player2Score = 5;
		player3Score = 5;
		player4Score = 5;
	    rand = 1;
		
		oneAlive = true;
		twoAlive = true;
		threeAlive = true;
		fourAlive = true;
		isGameOver = false;
		started = false;
		change = false;
		forced = false;
		powerUp.visible = false;
		random = RandomNumberGenerator.getRandInt(2)+1;
		
		switch(random)
		{
		case 1:
			{
				path = "/resources/" + "gamePlayMusic.wav";
				break;
			}
		case 2:
		{
			path = "/resources/" + "gamePlayMusic1.wav";
		}
	}
		
	   if(gamePlayMusic && !already)
			{
			
			  try {
				player.playMusic(path);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		musicOn = true;
		longestRally = 0;
		currentRally = 0;
		totalHits = 0;
		totalPoints = 0;
		deaths = 0;
		j = 0;
		i = 0;
		k = 0;
		i = 0;
		powerOnLeft = 0;
		powerOnRight = 0;
		powerOnTop = 0;
		powerOnBottom = 0;
		p = 0;
		flashLeft = 0;
		flashRight = 0;
		flashTop = 0;
		flashBottom = 0;
		turnOffPower();
	}
	
    @Override
	public void update() { 
    	
    	if(powerUpHit())powerUp.taken = true;
        if(ball.velX > 0)pos = true;
    	if(ball.velX < 0)pos = false;
    	
    	if(Math.abs(ball.velX) < 4)
    		{
              if(pos) ball.velX = 4;
    		  else if(!pos) ball.velX = -4;
    		}
  
        ball.update();
    	if(!isGameOver && powerUps) checkForPowerUps();
    	
    	if(p == 500)
        {
        	if(powerUps)powerUp.reset();
        	resetLives();
        	p = 0;
        }
        
    	p++;
        if(powerUps)powerUp.update();
    	if(!started) j++;
	    
    	if(isGameOver)
			{
    		  i++;
			  change = false;
			 }
		
		if(!oneAlive && !isGameOver) k++;
			
		if(deaths == 3) isGameOver = true;
		
		randomizeIntelligence();
		
	//check for collisions of paddles with ball
		leftPaddleCollisions();
		rightPaddleCollisions();
		topPaddleCollisions();
		bottomPaddleCollisions();
	   
		 if (ball.isDead())
		   {
			  ball.reset();
			  rand = 1;
		   }
	}

    @Override
	public void render(Graphics g) {
		
		// draw background
		if(!red)
			{
			 g.setColor(Resources.blue);
			 g.fillRect(0, 0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
			}
			
		else if(red)g.drawImage(Resources.redBack, 0, 0, null); 
		
		// draw "net"
		g.drawImage(Resources.line, (GameMain.GAME_WIDTH / 2) - 2, 0, null);
		
		//draw rectangle behind stats
		if(!red)g.setColor(Resources.blue);
		else g.setColor(Resources.red);
		if(isGameOver)g.fillRect(325,315,150,60);
		
		//check if any paddle should be frozen or invincible
		checkIfFrozen();
		checkIfInvincible(g);
		
		
		//show paddles
		drawLeftPaddle(g);
	    drawRightPaddle(g);
		drawTopPaddle(g);
		drawBottomPaddle(g);
		
	    // Draw Ball
		g.drawImage(Resources.ball, ball.getX(), ball.getY(), null);
        //Draw powerUp
		if(!powerUp.taken)displayPowerUp(g);
		// show scores
		displayScore(g);
        //3 2 1... go!!
		if(!started)readyGo(Game.gameImage.getGraphics());
		
		//forcing...
		if(!oneAlive && !isGameOver && k >= 60 && k <= 100) displayForceOption(g); 
			
		 //flashing play again && showing "return to menu"
		if(isGameOver)
			 {
			   displayMenuOption(g);
			   if(i >= 60 && i <= 100) displayPlayAgain(g);
			 }
	    	
		 //display statistics
		 if(isGameOver) displayStats(g);
		 if(deaths == 3 || forced) displayWinner(g); 
	}
    
    private void checkIfInvincible(Graphics g)
    {
    	if(powerOnLeft <= 800 && oneAlive && paddleLeft.isHit() && paddleLeft.isInvincible())
		{
		    if(powerOnLeft < 600)displayBricks(g);
            if(powerOnLeft >= 600 && powerOnLeft <= 799 && flashLeft >= 20 && flashLeft <= 40)
    		{
    			displayBricks(g);
    			if(flashLeft == 40) flashLeft = 0;
    		}
    		if(powerOnLeft >= 600)flashLeft++;
    		
    		if(powerOnLeft == 800)
		   {
			   paddleLeft.invincible = false;
			   paddleLeft.hit = false;
			   powerOnLeft = 0;
			   flashLeft = 0;
		   }
		 powerOnLeft++;
		}
    	
    	else if(powerOnRight <= 800 && twoAlive && paddleRight.isHit() && paddleRight.isInvincible())
		{
		    if(powerOnRight < 600)displayBricks(g);
            if(powerOnRight >= 600 && powerOnRight <= 799 && flashRight >= 20 && flashRight <= 40)
    		{
    			displayBricks(g);
    			if(flashRight == 40) flashRight = 0;
    		}
    		if(powerOnRight >= 600)flashRight++;
    		
    		if(powerOnRight == 800)
		   {
			    paddleRight.invincible = false;
    			paddleRight.hit = false;
    			powerOnRight = 0;
    			flashRight = 0;
		   }
    		powerOnRight++;
		}
    	
    	else if(powerOnTop <= 800 && threeAlive && paddleTop.isHit() && paddleTop.isInvincible())
		{
		    if(powerOnTop < 600)displayBricks(g);
            if(powerOnTop >= 600 && powerOnTop <= 799 && flashTop >= 20 && flashTop <= 40)
    		{
    			displayBricks(g);
    			
    			if(flashTop == 40) flashTop = 0;
    		}
    		if(powerOnTop >= 600)flashTop++;
    		
    		if(powerOnTop == 800)
		   {
			    paddleTop.invincible = false;
    			paddleTop.hit = false;
    			powerOnTop = 0;
    			flashTop = 0;
		   }
    		powerOnTop++;
		}
    	
    	else if(powerOnBottom <= 800 && fourAlive && paddleBottom.isHit() && paddleBottom.isInvincible())
		{
		    if(powerOnBottom < 600)displayBricks(g);
            if(powerOnBottom >= 600 && powerOnBottom <= 799 && flashBottom >= 20 && flashBottom <= 40)
    		{
    			displayBricks(g);
    			if(flashBottom == 40) flashBottom = 0;
    		}
    		if(powerOnBottom >= 600)flashBottom++;
    		
    		if(powerOnBottom == 800)
		   {
			    paddleBottom.invincible = false;
    			paddleBottom.hit = false;
    			powerOnBottom = 0;
    			flashBottom = 0;
		   }
    		powerOnBottom++;
		}
	}
	
	private void checkIfFrozen()
	{
		if(powerOnLeft <= 300 && oneAlive && paddleLeft.isHit() && paddleLeft.isFrozen())
		{
		   paddleLeft.frozen();
		   if(powerOnLeft == 300)
		   {
			   paddleLeft.frozen = false;
			   paddleLeft.hit = false;
			   powerOnLeft = 0;
			  if(soundFX) Resources.iceBreak.play();
		   }
		 powerOnLeft++;
		}
	
	else if(powerOnRight <= 300 && twoAlive && paddleRight.isHit() && paddleRight.isFrozen())
	{
		paddleRight.frozen();
	   if(powerOnRight == 300)
	   {
		   paddleRight.frozen = false;
		   paddleRight.hit = false;
		   powerOnRight = 0;
		   if(soundFX)  Resources.iceBreak.play();
	   }
	   powerOnRight++;
	}
	
	else if(powerOnTop <= 300 && threeAlive && paddleTop.isHit() && paddleTop.isFrozen())
	{
		paddleTop.frozen();
	   if(powerOnTop == 300)
	   {
		   paddleTop.frozen = false;
		   paddleTop.hit = false;
		   powerOnTop = 0;
		   if(soundFX) Resources.iceBreak.play();
	   }
	   powerOnTop++;
	}
	
	else if(powerOnBottom <= 300 && fourAlive && paddleBottom.isHit() && paddleBottom.isFrozen())
	{
		paddleBottom.frozen();
	   if(powerOnBottom == 300)
	   {
		   paddleBottom.frozen = false;
		   paddleBottom.hit = false;
		   powerOnBottom = 0;
		   if(soundFX) Resources.iceBreak.play();
	   }
	   powerOnBottom++;
	}
}
	private void drawLeftPaddle(Graphics g)
	{
		g.setColor(Color.white);
		
		if(paddleLeft.isFrozen() && oneAlive) g.drawImage(Resources.leftFrozen,paddleLeft.getX()-6, paddleLeft.getY(), null);
		
		else if((oneAlive && !paddleLeft.isHit()) || (paddleLeft.isInvincible() && oneAlive)) g.fillRect(paddleLeft.getX(), paddleLeft.getY(), paddleLeft.getWidth(),paddleLeft.getHeight());
		else if(powerOnLeft <= 800 && oneAlive && paddleLeft.isHit() && paddleLeft.isGrown())
		 {
			if(powerOnLeft < 600)g.fillRect(paddleLeft.getBigRect().x, paddleLeft.getBigRect().y, paddleLeft.getBigRect().width, paddleLeft.getBigRect().height);
		     //flashing
			else if(powerOnLeft >= 600 && powerOnLeft <= 799 && flashLeft >= 20 && flashLeft <= 40)
			{
				g.fillRect(paddleLeft.getBigRect().x, paddleLeft.getBigRect().y, paddleLeft.getBigRect().width, paddleLeft.getBigRect().height);
				
				if(flashLeft== 40)flashLeft = 0;
			}
			
			if(powerOnLeft >= 600)flashLeft++;
		
			 if(powerOnLeft == 800)
				 {
				  paddleLeft.grown = false;
				  paddleLeft.hit = false;
				  powerOnLeft = 0;
				  flashLeft = 0;
				 }
			 powerOnLeft++;
		 }
		else if(powerOnLeft <= 600 && oneAlive && paddleLeft.isHit() && paddleLeft.isShrunk())
		{
			g.fillRect(paddleLeft.getSmallRect().x, paddleLeft.getSmallRect().y, paddleLeft.getSmallRect().width, paddleLeft.getSmallRect().height);
			 if(powerOnLeft == 600)
				 {
				  paddleLeft.shrunk = false;
				  paddleLeft.hit = false;
				  powerOnLeft = 0;
				 }
			 powerOnLeft++;
		}
	}
	
	private void drawRightPaddle(Graphics g)
	{
        g.setColor(Resources.yellow);
		
        if(paddleRight.isFrozen() && twoAlive) g.drawImage(Resources.rightFrozen,paddleRight.getX()-6, paddleRight.getY(), null);
        
        else if((twoAlive && !paddleRight.isHit()) || (paddleRight.isInvincible() && twoAlive))g.fillRect(paddleRight.getX(), paddleRight.getY(),paddleRight.getWidth(), paddleRight.getHeight());
        else if(powerOnRight <= 800 && twoAlive && paddleRight.isHit() && paddleRight.isGrown())
		 {
			if(powerOnRight < 600)g.fillRect(paddleRight.getBigRect().x, paddleRight.getBigRect().y, paddleRight.getBigRect().width, paddleRight.getBigRect().height);
		     //flashing
			else if(powerOnRight >= 600 && powerOnRight <= 799 && flashRight >= 20 && flashRight <= 40)
			{
				g.fillRect(paddleRight.getBigRect().x, paddleRight.getBigRect().y, paddleRight.getBigRect().width, paddleRight.getBigRect().height);
				
				if(flashRight == 40)flashRight = 0;
			}
			if(powerOnRight >= 600)flashRight++;
		
			 if(powerOnRight == 800)
				 {
				 paddleRight.grown = false;
				 paddleRight.hit = false;
				  powerOnRight = 0;
				  flashRight = 0;
				 }
			 powerOnRight++;
		 }
		else if(powerOnRight <= 600 && twoAlive && paddleRight.isHit() && paddleRight.isShrunk())
		{
			g.fillRect(paddleRight.getSmallRect().x, paddleRight.getSmallRect().y, paddleRight.getSmallRect().width, paddleRight.getSmallRect().height);
			 if(powerOnRight == 600)
				 {
				 paddleRight.shrunk = false;
				 paddleRight.hit = false;
				  powerOnRight = 0;
				 }
			 powerOnRight++;
			}
	}
	
	private void drawTopPaddle(Graphics g)
	{
        g.setColor(Color.red);
        
        if(paddleTop.isFrozen() && threeAlive) g.drawImage(Resources.topFrozen,paddleTop.getX(), paddleTop.getY()-6, null);
		
        else if((threeAlive && !paddleTop.isHit()) || (paddleTop.isInvincible() && threeAlive))g.fillRect(paddleTop.getX(), paddleTop.getY(),paddleTop.getWidth(), paddleTop.getHeight());
       //big
        else if(powerOnTop <= 800 && threeAlive && paddleTop.isHit() && paddleTop.isGrown())
		 {
			if(powerOnTop < 600)g.fillRect(paddleTop.getBigRect().x, paddleTop.getBigRect().y, paddleTop.getBigRect().width, paddleTop.getBigRect().height);
		     //flashing
			else if(powerOnTop >= 600 && powerOnTop <= 799 && flashTop >= 20 && flashTop <= 40)
			{
				g.fillRect(paddleTop.getBigRect().x, paddleTop.getBigRect().y, paddleTop.getBigRect().width, paddleTop.getBigRect().height);
				
				if(flashTop== 40)flashTop = 0;
			}
			
			if(powerOnTop >= 600)flashTop++;
		
			 if(powerOnTop == 800)
				 {
				 paddleTop.grown = false;
				 paddleTop.hit = false;
				 powerOnTop = 0;
				  flashTop = 0;
				 }
			 powerOnTop++;
		 }
        //small
		else if(powerOnTop <= 600 && threeAlive && paddleTop.isHit() && paddleTop.isShrunk())
		{
			g.fillRect(paddleTop.getSmallRect().x, paddleTop.getSmallRect().y, paddleTop.getSmallRect().width, paddleTop.getSmallRect().height);
			 if(powerOnTop == 600)
				 {
				  paddleTop.shrunk = false;
				  paddleTop.hit = false;
				  powerOnTop = 0;
				 }
			  powerOnTop++;
			}
	}
	
	private void drawBottomPaddle(Graphics g)
	{
		
        g.setColor(Resources.green);
		
        if(paddleBottom.isFrozen() && fourAlive) g.drawImage(Resources.bottomFrozen,paddleBottom.getX(), paddleBottom.getY()-6, null);
        
        else if((fourAlive && !paddleBottom.isHit()) || (paddleBottom.isInvincible() && fourAlive))g.fillRect(paddleBottom.getX(), paddleBottom.getY(),paddleBottom.getWidth(), paddleBottom.getHeight());
        else if(powerOnBottom <= 800 && fourAlive && paddleBottom.isHit() && paddleBottom.isGrown())
		 {
			if(powerOnBottom < 600)g.fillRect(paddleBottom.getBigRect().x, paddleBottom.getBigRect().y, paddleBottom.getBigRect().width, paddleBottom.getBigRect().height);
		     //flashing
			else if(powerOnBottom >= 600 && powerOnBottom <= 799 && flashBottom >= 20 && flashBottom <= 40)
			{
				g.fillRect(paddleBottom.getBigRect().x, paddleBottom.getBigRect().y, paddleBottom.getBigRect().width, paddleBottom.getBigRect().height);
				
				if(flashBottom == 40)flashBottom = 0;
			}
			if(powerOnBottom >= 600)flashBottom++;
		
			 if(powerOnBottom == 800)
				 {
				 paddleBottom.grown = false;
				 paddleBottom.hit = false;
				 powerOnBottom = 0;
				 flashBottom = 0;
				 }
			 powerOnBottom++;
		 }
	    else if(powerOnBottom <= 600 && fourAlive && paddleBottom.isHit() && paddleBottom.isShrunk())
		{
			g.fillRect(paddleBottom.getSmallRect().x, paddleBottom.getSmallRect().y, paddleBottom.getSmallRect().width, paddleBottom.getSmallRect().height);
			 if(powerOnBottom == 600)
				 {
				 paddleBottom.shrunk = false;
				 paddleBottom.hit = false;
				 powerOnBottom = 0;
				 }
			 powerOnBottom++;
		}
	}
	
	private void randomizeIntelligence() {
		paddleLeft.update();
		
		
		if(change)
			{
			   if(!paddleRight.isFrozen()) paddleRight.changeIntelligence(rand);
			   if(!paddleTop.isFrozen())paddleTop.changeIntelligence(rand);
			   if(!paddleBottom.isFrozen())paddleBottom.changeIntelligence(rand);
			}
		else 
			{
			   if(!paddleRight.isFrozen())paddleRight.update();
			   if(!paddleTop.isFrozen()) paddleTop.update();
			   if(!paddleBottom.isFrozen()) paddleBottom.update();
			}
	}
	
	private void resetLives()
	{
		paddleLeft.plus = false;
		paddleRight.plus = false;
		paddleTop.plus = false;
		paddleBottom.plus = false;
	}
	
	private void displayBricks(Graphics g)
	{
		if(paddleLeft.isInvincible() && oneAlive)g.drawImage(Resources.brickLeft, 0, 0, null);
		else if(paddleRight.isInvincible() && twoAlive)g.drawImage(Resources.brickRight, 785, 0, null);
		else if(paddleTop.isInvincible() && threeAlive)g.drawImage(Resources.brickTopBottom, 0, 0, null);
		else if(paddleBottom.isInvincible() && fourAlive)g.drawImage(Resources.brickTopBottom, 0, 435, null);
	}
	
	private void displayScore(Graphics g)
	{
		g.setColor(Color.white);
		g.setFont(scoreFont); // Sets scoreFont as current font
		g.drawString("" + player1Score, 320, 40);
		if(!oneAlive && red)g.drawImage(Resources.whiteDeadred, 320, 18, null);
		else if(!oneAlive && !red)g.drawImage(Resources.whiteDeadblue, 320, 18, null);
		g.setColor(Resources.yellow);
		g.drawString("" + player2Score, 360, 40);
		if(!twoAlive && red)g.drawImage(Resources.yellowDeadred, 360, 18, null);
		else if (!twoAlive && !red)g.drawImage(Resources.yellowDeadblue, 360, 18, null);
		g.setColor(Color.red);
		g.drawString("" + player3Score, 420, 40);
		if(!threeAlive  && red)g.drawImage(Resources.redDeadred, 415, 18, null);
		else if(!threeAlive && !red)g.drawImage(Resources.redDeadblue, 415, 18, null);
		g.setColor(Resources.green);
		g.drawString("" + player4Score, 460, 40);
		if(!fourAlive && red)g.drawImage(Resources.greenDeadred, 455, 18, null);
		else if(!fourAlive && !red)g.drawImage(Resources.greenDeadblue, 455, 18, null);
	}
	
	private void displayPowerUp(Graphics g)
	{
		if(powerUp.visible && !isGameOver && powerUp.isGrow())g.drawImage(Resources.grows, powerUp.getX(), powerUp.getY(), null);
		else if(powerUp.visible && !isGameOver && powerUp.isShrink())g.drawImage(Resources.shrinks, powerUp.getX(), powerUp.getY(), null);
		else if(powerUp.visible && !isGameOver && powerUp.isFreeze())g.drawImage(Resources.freezes, powerUp.getX(),powerUp.getY(), null);
		else if(powerUp.visible && !isGameOver && powerUp.isLife())g.drawImage(Resources.life,powerUp.getX(),powerUp.getY(), null);
	    else if(powerUp.visible && !isGameOver && powerUp.isInvincibility())g.drawImage(Resources.invincible,powerUp.getX(),powerUp.getY(), null);
	}
	
	
	private void displayForceOption(Graphics g)
	{
		   g.setColor(Color.white);
           g.drawString("Press F to force a winner", 275, 230);
		   if (k == 100) k = 0;
	}
	
	private void displayMenuOption(Graphics g)
	{
           g.setFont(statsFont);
		   g.setColor(Color.yellow);
		   g.drawString("Press M to return to Main Menu", 10, 440);
	}
	
	private void displayWinner(Graphics g)
	 {
		 g.setFont(scoreFont);
		 //draw winner name
		if(oneAlive)
			{
			 g.setColor(Color.white);
			 g.drawString("Player 1 Wins!", 322, 200);
			}
		else if(twoAlive)
			{
			  g.setColor(Resources.yellow);
			  g.drawString("Player 2 Wins!", 322, 200);
			}
		else if(threeAlive)
			{
			 g.setColor(Color.red);
			 g.drawString("Player 3 Wins!", 322, 200);
			}
		else if(fourAlive)
			{
			  g.setColor(Resources.green);
			  g.drawString("Player 4 Wins!", 322, 200);
			}
	}
	
	private void displayStats(Graphics g)
	{
		 g.setFont(statsFont);
		 g.setColor(Color.gray);
		 g.drawString("Total points played: ", 330, 330);
		 g.drawString("Longest rally: ", 330, 350);
		 g.drawString("Average rally length: ", 330, 370);
		 if(oneAlive)g.setColor(Color.white);
		 if(twoAlive)g.setColor(Resources.yellow);
		 if(threeAlive)g.setColor(Color.red);
		 if(fourAlive)g.setColor(Resources.green);
		 g.drawString(""+totalPoints, 475, 330);
		 g.drawString(""+longestRally, 435, 350);
		 if(totalPoints == 0)totalPoints = 1;
		 g.drawString(""+totalHits/totalPoints, 480, 370);
	}
	
	private void readyGo(Graphics g)
	{
		ball.setX(385);
		ball.setY(210);
		g.setColor(Color.yellow);
		g.setFont(goFont); 
		
		if(j < 100) g.drawString("3", 382, 200);
		if(j >= 100 && j < 180) g.drawString("2", 382, 200);
		if(j >= 180 && j < 280) g.drawString("1", 382, 200);
		if(j > 280 && j < 380) 
			{
			 g.drawString("Go!", 352, 200);
			  if(j == 379)started = true;
			}
	}
	
	private void displayPlayAgain(Graphics g)
	 {
		  g.setFont(scoreFont);
		  g.setColor(Color.white);
   	      g.drawString("Press R to Play Again", 272, 280);
   	      
   	      if(i == 100) i = 0;
	 }
 	private void checkForPowerUps() {
		
		if(oneAlive) paddleLeft.checkForPowerUps();
    	if(twoAlive) paddleRight.checkForPowerUps();
    	if(threeAlive) paddleTop.checkForPowerUps();
    	if(fourAlive) paddleBottom.checkForPowerUps();
	}
	
	private void forceIt()
	{
		if(player2Score >= player3Score && player2Score >= player4Score)
		{
			isGameOver = true;
			threeAlive = false;
			fourAlive = false;
		}
		
		else if(player3Score >= player2Score && player3Score >= player4Score)
		{
			isGameOver = true;
			twoAlive = false;
			fourAlive = false;
		}
		
		else if(player4Score >= player3Score && player4Score >= player2Score)
		{
			isGameOver = true;
			threeAlive = false;
			twoAlive = false;
		}
		
		forced = true;
	}
 
	private boolean powerUpHit()
	 {
		 if(powerUpCollides(paddleLeft) || powerUpCollides(paddleRight) || powerUpCollides(paddleTop) || powerUpCollides(paddleBottom)) return true;
		 else return false;
	 }

	private boolean smallCollides(Paddle p){
		return ball.getRect().intersects(p.getSmallRect());
	}
	
	private boolean smallCollides(AIPaddle p) {
		return ball.getRect().intersects(p.getSmallRect());
	}
	
	private boolean smallCollides(AI2Paddle p) {
		return ball.getRect().intersects(p.getSmallRect());
	}
	
	private boolean smallCollides(AI3Paddle p) {
		return ball.getRect().intersects(p.getSmallRect());
	}
	
	private boolean normalCollides(AIPaddle p) {
		return ball.getRect().intersects(p.getRect());
	}
	
	public static boolean normalCollides(Paddle p) {
		return ball.getRect().intersects(p.getRect());
	}
	
	private boolean normalCollides(AI3Paddle p) {
		return ball.getRect().intersects(p.getRect());
	}
	
	private boolean normalCollides(AI2Paddle p) {
		return ball.getRect().intersects(p.getRect());
	}
	
	public boolean bigCollides(Paddle p) {
		return ball.getRect().intersects(p.getBigRect());
	}
	
	public boolean bigCollides(AIPaddle p) {
		return ball.getRect().intersects(p.getBigRect());
	}
	
	public boolean bigCollides(AI2Paddle p) {
		return ball.getRect().intersects(p.getBigRect());
	}
	
	public boolean bigCollides(AI3Paddle p) {
		return ball.getRect().intersects(p.getBigRect());
	}
	
	public static boolean powerUpCollides(AIPaddle p) {
		return powerUp.getRect().intersects(p.getRect());
	}
	
	public static boolean powerUpCollides(Paddle p) {
		return powerUp.getRect().intersects(p.getRect());
	}
	
	public static boolean powerUpCollides(AI3Paddle p) {
		return powerUp.getRect().intersects(p.getRect());
	}
	
	public static boolean powerUpCollides(AI2Paddle p) {
		return powerUp.getRect().intersects(p.getRect());
	}
	
	private void leftPaddleCollisions() {
		//normal collision with left
		   if ((paddleLeft.isFrozen() && normalCollides(paddleLeft)) || (!paddleLeft.isHit() && !paddleLeft.isGrown() && oneAlive && normalCollides(paddleLeft))) 
		   {
			     ball.onCollideWith(paddleLeft);
			     if(soundFX) Resources.hit.play();
				 change ^= true;
				 rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
		   }
		   
		   //collision with left when left is big
		   else if(oneAlive && paddleLeft.isHit() && paddleLeft.isGrown() &&  bigCollides(paddleLeft))
		   {
			     ball.onCollideWith(paddleLeft);
			     if(soundFX) Resources.hit.play();
				 change ^= true;
				 rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
		   }
		   
		   else if(oneAlive && paddleLeft.isHit() && paddleLeft.isShrunk() && smallCollides(paddleLeft))
		   {
			     ball.onCollideWith(paddleLeft);
			     if(soundFX) Resources.hit.play();
				 change ^= true;
				 rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
		   }
	}

	private void rightPaddleCollisions()
	{
		//normal collision with right
		   if ((paddleRight.isFrozen() && normalCollides(paddleRight))||(!paddleRight.isHit() && !paddleRight.isGrown() && twoAlive && normalCollides(paddleRight))) 
	       {
			   
			    ball.onCollideWith(paddleRight);
			    if(soundFX)  Resources.hit.play();
                if (!oneAlive)
                 {
			        change ^= true;
			        rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
			      } 
			      
		   }
		    
		 //collision with right when right is big
		    else if(twoAlive && paddleRight.isHit() && paddleRight.isGrown() &&  bigCollides(paddleRight))
		   {
		    	if (!oneAlive) {
		          
		          change ^= true;
		          rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
		        } 
		        ball.onCollideWith(this.paddleRight);
		        if(soundFX) Resources.hit.play();
		   }
		   
		    else if(twoAlive && paddleRight.isHit() && paddleRight.isShrunk() &&  smallCollides(paddleRight))
			   {
                     if (!oneAlive) {
			          
			          change ^= true;
			          rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
			        } 
			        ball.onCollideWith(this.paddleRight);
			        if(soundFX)  Resources.hit.play();
			   }
		   }
	
	  private void topPaddleCollisions()
	  {
		//normal collision with top
		   if ((paddleTop.isFrozen() && normalCollides(paddleTop)) ||(!paddleTop.isHit() && !paddleTop.isGrown() && threeAlive && normalCollides(paddleTop))) 
	       {
                  if (!oneAlive && !twoAlive) {
			        
			        change ^= true;
			        rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
			      } 
			      ball.onCollideWith(this.paddleTop);
			      if(soundFX)  Resources.hit.play();
	       }
		   
		   //collision with top when top is big
		   else if(threeAlive && paddleTop.isHit() && paddleTop.isGrown() &&  bigCollides(paddleTop))
		   {
                  if (!oneAlive && !twoAlive) {
			        
			        change ^= true;
			        rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
			      } 
			      ball.onCollideWith(this.paddleTop);
			      if(soundFX)  Resources.hit.play();
		   }
		   
		   else if(threeAlive && paddleTop.isHit() && paddleTop.isShrunk() &&  smallCollides(paddleTop))
		   {
                   if (!oneAlive && !twoAlive) {
			        
			        change ^= true;
			        rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
			      } 
			      ball.onCollideWith(this.paddleTop);
			      if(soundFX)  Resources.hit.play();
		   }
	}

	private void bottomPaddleCollisions()
	{
		   //normal collision with bottom
		   if ((paddleBottom.isFrozen() && normalCollides(paddleBottom)) ||(!paddleBottom.isHit() && !paddleBottom.isGrown() && fourAlive && normalCollides(paddleBottom)))
		   {
			   ball.onCollideWith(this.paddleBottom);
			   if(soundFX) Resources.hit.play();
		   }
		   
		   //collision with bottom when bottom is big
		   else if(fourAlive && paddleBottom.isHit() && paddleBottom.isGrown() &&  bigCollides(paddleBottom))
		   {
			   ball.onCollideWith(this.paddleBottom);
			   if(soundFX)  Resources.hit.play();
		   }	
		   
		   else if(fourAlive && paddleBottom.isHit() && paddleBottom.isShrunk() &&  smallCollides(paddleBottom))
		   {
			   ball.onCollideWith(this.paddleBottom);
			   if(soundFX) Resources.hit.play();
		   }	   
		 }
	
	private void turnOffPower()
	{
		paddleLeft.hit = false;
		paddleRight.hit = false;
		paddleTop.hit = false;
		paddleBottom.hit = false;
		paddleLeft.frozen = false;
		paddleRight.frozen = false;
		paddleTop.frozen = false;
		paddleBottom.frozen = false;
		paddleLeft.invincible = false;
		paddleRight.invincible = false;
		paddleTop.invincible = false;
		paddleBottom.invincible = false;
		paddleLeft.grown = false;
		paddleRight.grown = false;
		paddleTop.grown = false;
		paddleBottom.grown = false;
		paddleLeft.shrunk = false;
		paddleRight.shrunk = false;
		paddleTop.shrunk = false;
		paddleBottom.shrunk = false;
	}
	
	@Override
	public void onClick(MouseEvent e) {}

	@Override
	public void onKeyRelease(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_UP|| e.getKeyCode() == KeyEvent.VK_DOWN) paddleLeft.stop();
	}
	
	@Override
	public void onKeyPress(KeyEvent e) {
		
		if (!paddleLeft.frozen && e.getKeyCode() == KeyEvent.VK_UP) paddleLeft.accelUp();
			
		if (!paddleLeft.frozen && e.getKeyCode() == KeyEvent.VK_DOWN) paddleLeft.accelDown();
			
		//if (e.getKeyCode() == KeyEvent.VK_SPACE) ball.reset();
		if(e.getKeyCode() == KeyEvent.VK_P)
		{
			
		
			if(!GameMain.sGame.isPaused)
			{
			  GameMain.sGame.pauseGame();
			}
		    else GameMain.sGame.resumeGame();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_R) 
			{
			  // Resources.clip.stop();
			  already = true; 
			  setCurrentState(new PlayState());
			}
		
		if (e.getKeyCode() == KeyEvent.VK_F && !oneAlive) forceIt();
		
		if (e.getKeyCode() == KeyEvent.VK_M)
			{
			 setCurrentState(new MenuState());
			 Resources.menuClip.stop();
			 if(MenuState.music)
			 {
				try {
					player.playMusic("/resources/" + "menu.wav");
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			 }
			}
	    
	}
}