package model;

import java.awt.Rectangle;

import main.GameMain;
import main.Resources;
import state.PlayState;

public class AI3Paddle {

	private int x, y, width, height, velX;
	private Rectangle rect,bigRect,smallRect;
	private static int MOVE_SPEED_X = 4;
	private boolean already = false;
	public static boolean hit,frozen,grown,shrunk,plus,invincible;

	public AI3Paddle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rectangle(x, y, width, height);
		bigRect = new Rectangle(x,y,width+40,height);
		smallRect = new Rectangle(x,y,width-30,height);
	}

	public void update() {
		
		x = PlayState.ball.getX();
		//x +=velX;
		checkIfDead();
		if(PlayState.fourAlive)dontLeaveTheScreen();
		else destroyBox();
	}
	
	private void checkIfDead()
	{
		if(PlayState.player4Score <= 0) 
		{
		  PlayState.fourAlive = false;
		  
		  if(!already)
		  {
		    PlayState.deaths++;
		    already = true;
		  }
		}
	}
	
	public void frozen(){
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
			 if(PlayState.medium)x = PlayState.ball.getX()-57;
			 if(PlayState.easy)x = PlayState.ball.getX()-65;
			 if(PlayState.hard) x = PlayState.ball.getX()-35;
			 dontLeaveTheScreen();
			 break;
		 }
			
		 case 2:{
			 if(PlayState.medium)x = PlayState.ball.getX()-35;
			 if(PlayState.easy)x = PlayState.ball.getX()+70;
			 if(PlayState.hard) x = PlayState.ball.getX()-43;
			 dontLeaveTheScreen();
			 break;
		 }
			
		 case 3: {
			 
			 if(PlayState.medium || PlayState.easy)x = PlayState.ball.getX()-70;
			 if(PlayState.hard) x = PlayState.ball.getX()-37;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 4:{
			 
			 if(PlayState.medium)x = PlayState.ball.getX()-43;
			 if(PlayState.easy)x = PlayState.ball.getX()-70;
			 if(PlayState.hard) x = PlayState.ball.getX()+26;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 5: {
			 
			 if(PlayState.medium || PlayState.easy || PlayState.hard)x = PlayState.ball.getX() - 25;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 6:
		 {
			 if(PlayState.medium || PlayState.easy)x = PlayState.ball.getX() - 18;
			 if(PlayState.hard) x = PlayState.ball.getX()-30;
			 dontLeaveTheScreen();
			 break;
		 }
		 
		 case 7:{
			 if(PlayState.medium || PlayState.easy)x = PlayState.ball.getX() + 45;
			 if(PlayState.hard) x = PlayState.ball.getX()-20;
			 dontLeaveTheScreen();
			 break;
		 }
		}
      }
	
	private void dontLeaveTheScreen()
	{
		if (x < 0) x = 0;
		 
		else if (!shrunk && !grown && x + width > GameMain.GAME_WIDTH) x = GameMain.GAME_WIDTH - width;
		else if(hit && isGrown() && x + getBigRect().width > GameMain.GAME_WIDTH) x = GameMain.GAME_WIDTH - getBigRect().width;
		else if(hit && isShrunk() && x + getSmallRect().width > GameMain.GAME_WIDTH) x = GameMain.GAME_WIDTH - getSmallRect().width;
		
		if(PlayState.fourAlive)
    	   {
    	     updateRect();
    	     updateBigRect();
    	     updateSmallRect();
    	   }
	}
	
	public void checkForPowerUps()
	{
		if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isGrow()) 
		{     if(PlayState.soundFX)Resources.grow.play();
			  grown = true;
			  hit = true;
			  System.out.println("Player 4 got bigger");
		}
		if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isFreeze())
		{
			if(PlayState.soundFX)Resources.freeze.play();
			frozen = true;
			hit = true;
			System.out.println("Player 4 got frozen");
		}
		if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isShrink())
		{
			if(PlayState.soundFX)Resources.shrink.play();
			 shrunk = true;
			 hit = true;
			 System.out.println("Player 4 got smaller");
		}
		else if (!plus && PlayState.player4Score!=5 && PlayState.powerUpCollides(this) && PlayState.powerUp.isLife()) 
		{
			
			if(PlayState.soundFX)Resources.lifeUp.play();
			  PlayState.player4Score++;
			  plus = true;
			  System.out.println("Player 4 life++");
			  PlayState.powerUp.already4++;
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isInvincibility())
		{
			if(PlayState.soundFX)Resources.invincibility.play();
			invincible = true;
			hit = true;
			System.out.println("Player 4 became invincible");
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
	     bigRect.setBounds(x, y, width+40, height);
	}
	
	 private void updateSmallRect(){
	    	smallRect.setBounds(x, y, width-30, height);
	    }
	 
	 public void accelUp() {
			velX = -MOVE_SPEED_X;
		}

		public void accelDown() {
			velX = MOVE_SPEED_X;
		}

		public void stop() {
			velX = 0;
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

	public Rectangle getSmallRect(){
		return smallRect;
	}
}