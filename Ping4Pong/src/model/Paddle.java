package model;

import java.awt.Rectangle;

import main.GameMain;
import main.Resources;
import state.PlayState;

public class Paddle {
	private int x, y, width, height, velY;
	private Rectangle rect,bigRect,smallRect;
	private static int MOVE_SPEED_Y = 4;
	private boolean already;
	public static boolean hit,frozen,grown,shrunk,plus,invincible,fast,slow;

	public Paddle(int x, int y, int width, int height) {
		
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
		
		if(PlayState.isGameOver)y = PlayState.ball.getY();
	    else y += velY;
	    //y = PlayState.ball.getY();//be unbeatable
		checkIfDead();
        if(PlayState.oneAlive)dontLeaveTheScreen();
        else destroyBox();
	}
	
	
	private void checkIfDead()
	{
		if(PlayState.player1Score <= 0) 
		{
		   PlayState.oneAlive = false;
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
	
	public void dontLeaveTheScreen()
	{
		if (y < 0) y = 0;
		
		
	    else if (!hit &&  y + height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - height;
	    else if(hit && isInvincible() && y + height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - height;
	    else if(hit && isGrown() && y + getBigRect().height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - getBigRect().height;
	    else if(hit && isShrunk() && y + getSmallRect().height > GameMain.GAME_HEIGHT) y = GameMain.GAME_HEIGHT - getSmallRect().height;
		
		if(PlayState.oneAlive)
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
			  System.out.println("Player 1 got bigger");
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isFreeze())
		{
			if(PlayState.soundFX) Resources.freeze.play();
			  frozen = true;
		      hit = true;
		      System.out.println("Player 1 got frozen");
		      
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isShrink())
		{
			if(PlayState.soundFX) Resources.shrink.play();
			  shrunk = true;
			  hit = true;
			  System.out.println("Player 1 got smaller");
		}
		else if (!plus && PlayState.player1Score!=5  && PlayState.powerUpCollides(this) && PlayState.powerUp.isLife())
		{     
			if(PlayState.soundFX) Resources.lifeUp.play();
			  PlayState.player1Score++;
			  plus = true;
			  System.out.println("Player 1 life++");
			  PlayState.powerUp.already4++;
		}
		else if (!hit && PlayState.powerUpCollides(this) && PlayState.powerUp.isInvincibility())
		{
			if(PlayState.soundFX)Resources.invincibility.play();
			invincible = true;
			hit = true;
			System.out.println("Player 1 became invincible");
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