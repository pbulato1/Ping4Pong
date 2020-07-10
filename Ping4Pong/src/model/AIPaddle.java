package model;

import java.awt.Rectangle;

import framework.RandomNumberGenerator;
import main.GameMain;
import main.Resources;
import model.Ball;
import state.PlayState;
public class AIPaddle {

	private int x, y, width, height, velY;
	private Rectangle rect,bigRect,smallRect;
	private static int MOVE_SPEED_Y = 2;
	private boolean already;
	public static boolean hit,frozen,grown,shrunk,plus,invincible;

  public AIPaddle(int x, int y, int width, int height) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rectangle(x, y, width, height);
		bigRect = new Rectangle(x,y,width,height+40);
		smallRect = new Rectangle(x,y,width,height-30);
		velY = 0;
	}

	public void update() {
		
	    //y += velY;
		y = PlayState.ball.getY();
		checkIfDead();
	    if(PlayState.twoAlive)dontLeaveTheScreen();
	    else destroyBox();
      }
	
	private void checkIfDead()
	{
		if(PlayState.player2Score <= 0) 
		{
		  PlayState.twoAlive = false;
		  if(!already)
			  {
			    PlayState.deaths++;
			    already = true;
			  }
		  }
	  }
	
	public void frozen()
	{
		y = y;
	}
	
	private void destroyBox()
	{
		rect.setBounds(0,0,0,0);
		y = -100; 
	}
	
	public void changeIntelligence(int rand)
	{
		checkIfDead();
		
		switch(rand)
		{
		 case 1: {
			 
			 if(PlayState.easy)y = PlayState.ball.getY()-57;
			 if(PlayState.medium)y = PlayState.ball.getY()+25;
		     if(PlayState.hard) y = PlayState.ball.getY()+15;
			 dontLeaveTheScreen();
			 break;
		 }
			
		 case 2:{
			 if(PlayState.easy)y = PlayState.ball.getY()-65;
			 if(PlayState.medium)y = PlayState.ball.getY()-30;
			 if(PlayState.hard) y = PlayState.ball.getY()-20;
			 dontLeaveTheScreen();
			 break;
		 }
			
		 case 3: {
			 
			 if(PlayState.medium || PlayState.easy)y = PlayState.ball.getY()-70;
			 if(PlayState.hard) y = PlayState.ball.getY()+26;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 4:{
			 
			 if(PlayState.easy)y = PlayState.ball.getY()-73;
			 if(PlayState.medium)y = PlayState.ball.getY()-25;
			 if(PlayState.hard) y = PlayState.ball.getY()-33;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 5: {
			 
			 if(PlayState.medium || PlayState.easy)y = PlayState.ball.getY()+66;
			 if(PlayState.hard) y = PlayState.ball.getY()-40;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 6:
		 {
			 if(PlayState.medium || PlayState.easy)y = PlayState.ball.getY()-58;
			 if(PlayState.hard) y = PlayState.ball.getY()-35;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 7:{
			 if(PlayState.medium || PlayState.easy)y = PlayState.ball.getY()+45;
			 if(PlayState.hard) y = PlayState.ball.getY()-37;
			 dontLeaveTheScreen();
			 break;
		 }
	   }
	}
	
	private void dontLeaveTheScreen()
	{
		if (y < 0) y = 0;
			
		else if (!hit &&  y + height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - height;
	    else if(hit && isGrown() && y + getBigRect().height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - getBigRect().height;
	    else if(hit && isShrunk() && y + getSmallRect().height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - getSmallRect().height;

		if(PlayState.twoAlive)
			{
			 updateRect();
			 updateBigRect();
			 updateSmallRect();
			}
	}
	
	public void checkForPowerUps()
	{
		if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isGrow()) 
		{
			if(PlayState.soundFX) Resources.grow.play();
			  grown = true;
			  hit = true;
			  System.out.println("Player 2 got bigger");
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isFreeze())
		{
			if(PlayState.soundFX)Resources.freeze.play();
			  frozen = true;
			  hit = true;
			  System.out.println("Player 2 got frozen");
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isShrink())
		{
			if(PlayState.soundFX)Resources.shrink.play();
			  shrunk = true;
			  hit = true;
			  System.out.println("Player 2 got smaller");
		}
		else if (!plus && PlayState.player2Score!=5 && PlayState.powerUpCollides(this) && PlayState.powerUp.isLife())
		{
			if(PlayState.soundFX)Resources.lifeUp.play();
			  PlayState.player2Score++;
			  plus = true;
			  System.out.println("Player 2 life++");
			  PlayState.powerUp.already4++;
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isInvincibility())
		{
			if(PlayState.soundFX)Resources.invincibility.play();
              invincible = true;
			  hit = true;
			  System.out.println("Player 2 became invincible");
		}

	}
	
	public boolean isHit(){
		return hit;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public boolean isGrown(){
		return grown;
	}
	
	public boolean isShrunk(){
		return shrunk;
	}
	
	public boolean isInvincible(){
		return invincible;
	}
	
	
	private void updateRect() {
		rect.setBounds(x, y, width, height);
	}
	
	private void updateBigRect(){
    	bigRect.setBounds(x, y, width, height+40);
    }
	
	 private void updateSmallRect(){
	    	smallRect.setBounds(x, y, width, height - 30);
	}
	
	public void accelUp() {
		velY = -MOVE_SPEED_Y;
	}

	public void accelDown() {
		velY = MOVE_SPEED_Y;
	}
	
	public void stop() {
		velY = 0;
	}
	
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
	
	public Rectangle getBigRect() {
		return bigRect;
	}
	
	public Rectangle getSmallRect()
	{
		return smallRect;
	}
}