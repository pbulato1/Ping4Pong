package model;

import java.awt.Rectangle;

import framework.RandomNumberGenerator;
import state.PlayState;

public class PowerUp{
	
	int x,y,width,height;
	int velX,velY,randPow,already4,randDir;
	public static boolean visible,grow,shrink,freeze,life,invincibility,taken;
	
	Rectangle rect;
	public PowerUp(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rectangle(x,y,width,height);
	}
	
	public void update()
	{
		x += velX;
		y += velY;
		updateRect();
	}
	
	public Rectangle getRect()
	{
		return rect;
	}
	
	public void reset()
	{
		taken = false;
		visible = true;
		grow = false;
		shrink = false;
		freeze = false;
		life = false;
		invincibility = false;
		x = 380;
		y = 210;
	   if(PlayState.powerUps) randDir = RandomNumberGenerator.getRandInt(4)+1;
	   else if(!PlayState.powerUps) randDir = 8;
	   if(already4 < 4 ) randPow = RandomNumberGenerator.getRandInt(5)+1;
	   else randPow = RandomNumberGenerator.getRandInt(4)+1;
	   
	   switch(randDir)
	 {
	   case 1:
	   {
		  randomizePowerUp(randPow);
		  velY = 0;
		  velX = -2;
		  break; 
	   }
	   
	   case 2:
	   {
		   randomizePowerUp(randPow);
		   velX = 0;
		   velY = 2;
		   break; 
	   }
	   
	   case 3:
	   {
		   randomizePowerUp(randPow);
		   velY = 0;
		   velX = 2;
		   break; 
	   }
	   
	   case 4:
	   {
		   randomizePowerUp(randPow);
		   velX = 0;
		   velY = -2;
		   break; 
	   }
		   
	 }
	return ;
 }
	
	void randomizePowerUp(int randPow)
	{
		if(randPow == 1) freeze = true;
		if(randPow == 2) grow = true;
		if(randPow == 3) shrink = true;
		if(randPow == 4) invincibility = true;
		if(randPow == 5) life = true;
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

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	void updateRect(){
		rect.setBounds(x, y, width, height);
	}

	public boolean isFreeze(){
		return freeze;
	}
	
	public boolean isShrink(){
		return shrink;
	}
	
	public boolean isGrow(){
		return grow;
	}
	
	public boolean isLife(){
		return life;
	}
	
    public boolean isInvincibility(){
    	return invincibility;
    }
}