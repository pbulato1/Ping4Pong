package model;

import java.awt.Rectangle;

import framework.RandomNumberGenerator;
import main.GameMain;
import main.Resources;
import state.PlayState;

public class Ball {

	private int x, y, width, height, velY;
	public static int velX;
	private Rectangle rect;
	boolean changeDirect = true;

	public Ball(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		velX = 6;
		velY = RandomNumberGenerator.getRandIntBetween(-4, 5);
		rect = new Rectangle(x, y, width, height);
	}

	public void update() {
		
		x += velX;
		y += velY;
		correctYCollisions();
		correctXCollisions();
		updateRect();
	}

	private void correctYCollisions() {
		
		//three
		if (!PlayState.threeAlive && y < 0) y = 0; 
	    else if (PlayState.threeAlive && PlayState.paddleTop.isInvincible() && y < 15)
	    {
	    	
	    	if((PlayState.ball.getX() >= PlayState.paddleTop.getX()-10 && PlayState.ball.getX() <= PlayState.paddleTop.getX()+60))
	    		 {
	    		  if(PlayState.soundFX)Resources.hit.play();
	    		 }
	    		
	    	else 
	    		 {
	    		  if(PlayState.soundFX)Resources.brick.play();
	    		  System.out.println("Player 3 got saved by the wall");
	    		 }
   		 
	         y = 15;
	    	 velY = -velY;
	    	 
	        return;
	    }
	    //four
	    else if (!PlayState.fourAlive && y + height > 450) y = 430; 
	    else if (PlayState.fourAlive && PlayState.paddleBottom.isInvincible() && y + height > 435) 
	    	{
	    	   if((PlayState.ball.getX() >= PlayState.paddleBottom.getX()-10 && PlayState.ball.getX() <= PlayState.paddleBottom.getX()+60))
	    		{
	    		if(PlayState.soundFX) Resources.hit.play();
	    		}
	    	
	    	else
	    	{
	    		if(PlayState.soundFX)Resources.brick.play();
	    		System.out.println("Player 4 got saved by the wall");
	    		y = 415;
	    		velY = -velY;
	    		return;
	    	}
	      }
	    else return;
	    velY = -velY;
	    if(PlayState.soundFX) Resources.bounce.play();
	}
	
	public void correctXCollisions() {
		
		//one
		if (!PlayState.oneAlive && x < 0) x = 0; 
	    else if (PlayState.oneAlive && PlayState.paddleLeft.isInvincible() && x < 15)
	    {
	    	
	    	if((PlayState.ball.getY() < PlayState.paddleLeft.getY()) || (PlayState.ball.getY() > PlayState.paddleLeft.getY()+60))
	    	{
	    		if(PlayState.soundFX)Resources.brick.play();
	    		 System.out.println("Player 1 got saved by the wall");
	    	}
	    		  
	         else if(PlayState.soundFX)Resources.hit.play();
	    		 
	         x = 15;
	    	 velX = -velX;
	    	 PlayState.change ^= true;
			 PlayState.rand = RandomNumberGenerator.getRandIntBetween(1, 7) + 1;
	    	 
	        return;
	    }
	    //two
	    else if (!PlayState.twoAlive && x + width >= 800) x = 779;
	    else if (PlayState.twoAlive && PlayState.paddleRight.isInvincible() && x + width >= 785)
		{
			if((PlayState.ball.getY() < PlayState.paddleRight.getY()-10) || (PlayState.ball.getY() > PlayState.paddleRight.getY()+70))
	    	{
				if(PlayState.soundFX)Resources.brick.play();
	    		 System.out.println("Player 2 got saved by the wall");
	    		 x = 764;
	    		 velX = -velX;
	    		 return;
	    	}
	    }
	    else return;
	    
	    velX = -velX;
	    if(PlayState.soundFX) Resources.bounce.play();
	}

	private void updateRect() {
		rect.setBounds(x, y, width, height);
	}

	public void onCollideWith(Paddle p) {
		
		x = p.getX() + p.getWidth();
		velX = -velX;
		velY += RandomNumberGenerator.getRandIntBetween(-2, 3);
		updateStats();
	}
	
	public void onCollideWith(AIPaddle p) {
		
		x = p.getX() - width;
		velX = -velX;
		velY += RandomNumberGenerator.getRandIntBetween(-2, 3);
		updateStats();
	}
	
	public void onCollideWith(AI2Paddle p) {
		
	    y = p.getY() + height;
		velY = -velY;
		velX += RandomNumberGenerator.getRandIntBetween(-2, 3);
		updateStats();
	}
	
   public void onCollideWith(AI3Paddle p) {
		
	    y = p.getY() - height;
		velY = -velY;
		velX += RandomNumberGenerator.getRandIntBetween(-2, 3);
		updateStats();
	}
   
   public void updateStats()
   {
	   if(!PlayState.isGameOver)
		{
		  PlayState.totalHits++;
		 
	      PlayState.currentRally++;
	      if (PlayState.currentRally > PlayState.longestRally) PlayState.longestRally = PlayState.currentRally;
		}
   }

	public boolean isDead() {
		if(x <= 0 && PlayState.oneAlive) 
			{
			   PlayState.player1Score--;
			   System.out.println("Player 1 died, at "+ PlayState.rand);
			   PlayState.currentRally = 0;
			   PlayState.totalPoints++;
			   return true;
			 }
		else if (x + width >= GameMain.GAME_WIDTH && PlayState.twoAlive)
		{    
			 PlayState.player2Score--;
			 System.out.println("Player 2 died, at "+ PlayState.rand);
			 PlayState.currentRally = 0;
			 PlayState.totalPoints++;
			 return true;
		}
		else if(y <= 0 && PlayState.threeAlive)
			{
			  PlayState.player3Score--;
			  System.out.println("Player 3 died, at "+ PlayState.rand);
			  PlayState.currentRally = 0;
			  PlayState.totalPoints++;
			  return true;
			}
		
		else if(y + height >= GameMain.GAME_HEIGHT && PlayState.fourAlive)
			{
			  PlayState.player4Score--;
			  System.out.println("Player 4 died, at "+ PlayState.rand);
			  PlayState.currentRally = 0;
			  PlayState.totalPoints++;
			  return true;
			}
		
		return false;
	}

	public void reset() {
		
		if(changeDirect)x = 300;
		else x = 500;
		y = 200;
		if(changeDirect)velX = 6;
		else velX = -6;
		velY = RandomNumberGenerator.getRandIntBetween(-4, 5);
		changeDirect^= true;
		PlayState.change = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getRect() {
		return rect;
	}
}