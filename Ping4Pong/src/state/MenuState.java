package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.sound.sampled.LineUnavailableException;

import main.Resources;

public class MenuState extends State
{
	
  boolean pressedStart,pressedSettings,powOnOff,background,already,sound,fx,inGame,inMenu,kill,once,firstInit,help;
  static boolean music;
  private int currentChoice,blueRed,highLow = -1,fxi,inGamei,inMenui;
  private int offOn = -1;
  public static int initCount = 0;
  public static final Resources player = new Resources();
  String path = "/resources/" + "menu.wav";
  
  private String[] options = 
	  {
	  "Start", 
      "Help", 
      "Quit",
      "SETTINGS"
      };

  private String[] difficulty = 
	  {
	  "Easy", 
      "Medium", 
      "Hard" 
      };
  
  private String[] settings = 
	  {
	  "Background", 
      "Power-ups", 
      "Sound" 
      };
  private String[] onOff =
	  {
		"On",
		"Off"
	  };
  private String[] sounds = 
	  {
		"Sound FX",
		"Menu Music",
		"GamePlay Music"
	  };

  Font titleFont = new Font("Sanserif", 1, 40);
  Font scoreFont = new Font("SansSerif", Font.BOLD, 20);
  
  
  private void select() {
	  
    if (pressedStart &&  currentChoice == 0) 
    {
      PlayState.already = false;
      initCount++;
      
      if(music)Resources.menuClip.stop();
      setCurrentState(new PlayState());
     
    
      PlayState.easy = true;
      PlayState.medium = false;
      PlayState.change = false;
    } 
    if (pressedStart && currentChoice == 1)
    {
      PlayState.already = false;
      initCount++;
      if(music)Resources.menuClip.stop();
      setCurrentState(new PlayState());
      PlayState.medium = true;
      PlayState.easy = false;
      PlayState.hard = false;
    }
   
   if (!pressedStart && !pressedSettings && currentChoice == 1)help = true;
   if (!pressedStart && !pressedSettings && currentChoice == 2) System.exit(0);
   
    else if (pressedStart && !pressedSettings && currentChoice == 2)
    { 
      PlayState.already = false;
      initCount++;
      if(music)Resources.menuClip.stop();
      setCurrentState(new PlayState());
      
      PlayState.hard = true;
      PlayState.easy = false;
      PlayState.medium = false;
     }
  }

   public void init() {
	
	if(initCount == 0)PlayState.powerUps = true;
	if(initCount == 0 )PlayState.gamePlayMusic = true;
	if(initCount == 0)PlayState.soundFX = true;
    if(PlayState.musicOn && PlayState.gamePlayMusic)Resources.menuClip.stop();
  
    pressedStart = false;
   }
   
  void startMusic() throws LineUnavailableException{
	 player.playMusic(path);
}
  
  void stopMusic()
  {
	  Resources.menuClip.stop();
	  music = false;
  }

 public void update() {}

 public void render(Graphics g) {
	 
	 g.drawImage(Resources.welcome, 0, 0, null);
	 if(!pressedStart && !help)g.drawImage(Resources.screw, 310, 405, null);
	 if(!pressedStart && !help)g.drawImage(Resources.screw, 460, 405, null);
	 if(help)g.drawImage(Resources.help, 0, 0, null);
    
   if (!pressedStart && !pressedSettings && !help)
   {
      
      for (int i = 0; i < options.length; i++)
      {
    	  
        if (i == currentChoice)  g.setColor(Color.red); 
        else g.setColor(Color.white);
        g.setFont(titleFont);
        
        if(i == 3)
        	{
        	  g.setFont(scoreFont);
        	  if(currentChoice != 3)g.setColor(Color.CYAN);
        	  else g.setColor(Color.black);
        	  g.drawString(options[i], 350, 430);
        	}
        g.setFont(titleFont);
        if(i != 3)g.drawString(options[i], 350, 200 + i * 34);
        g.setColor(Color.yellow);
        g.drawString("Main Menu", 300, 135);
        g.setColor(Color.gray);
        g.setFont(scoreFont);
       
      }
    
    }
    else if (pressedStart)
    {
      
      for (int i = 0; i < difficulty.length; i++) 
      {
      
        if (i == currentChoice)  g.setColor(Color.red); 
        else g.setColor(Color.white); 
        g.setFont(titleFont);
        
         if (i == 1)  g.drawString(difficulty[1], 325, 236); 
         else   g.drawString(difficulty[i], 350, 200 + i * 35); 
         g.setColor(Color.yellow);
         g.drawString("Main Menu", 300, 135);
      } 
    } 
    
    else if(pressedSettings)
    {
    	g.drawImage(Resources.settings, 0, 0, null);
    	
    	for(int i = 0; i < settings.length; i++)
    	{
    
    		if(i == currentChoice && !powOnOff) g.setColor(Color.black);
    		
    		else g.setColor(Color.white);
    		g.setFont(scoreFont);
    		
    		g.drawString(settings[i], 15, 160 + i*35);
    		if(currentChoice == 0)
    		{ 
    			g.setColor(Color.red);
    			
    		   if(blueRed == 0 && background)g.drawImage(Resources.arrow, 285, 102, null);
    				
               if(blueRed == 1)g.drawImage(Resources.arrow, 585, 102, null);
    			
    			g.drawImage(Resources.redScreenShot, 170, 140, null);
        		g.drawImage(Resources.blueScreenShot, 470, 140, null);	
        		
        		if(!PlayState.red)g.drawImage(Resources.tick, 575, 190, null);
				
			else if(PlayState.red)g.drawImage(Resources.tick, 275, 190, null);
				
    		}
    		if(currentChoice == 1)
    		{
    			for(int j = 0; j < 2; j++ )
    			{
    			    if(j == offOn)g.setColor(Color.red);
    				else g.setColor(Color.white);
    				g.drawString(onOff[j], 160, 190 + j * 20);
    				if(PlayState.powerUps) g.drawImage(Resources.smallTick, 190, 167, null);
    				else if(!PlayState.powerUps) g.drawImage(Resources.smallTick, 190, 185, null);
    			}
    	    }
    		
    		if(currentChoice == 2)
    		{
    			for(int l = 0; l < sounds.length; l++)
    	    	{
    	    		if(l == highLow && !fx && !inGame && !inMenu) g.setColor(Color.yellow);
    	    		else g.setColor(Color.white);
    	    		g.setFont(scoreFont);
    	    		g.drawString(sounds[l], 190 + l * 150, 120);
    	    		
    	    		if(highLow == 0)
    	    		{
    	    			for(int f = 0; f < 2; f++ )
    	    			{
    	    				if(f == fxi) g.setColor(Color.red);
    	    				else g.setColor(Color.white);
    	    				g.drawString(onOff[f], 215, 155 + f * 20);
    	    				if(PlayState.soundFX) g.drawImage(Resources.smallTick, 245, 132, null);
    	    				else if(!PlayState.soundFX) g.drawImage(Resources.smallTick, 245, 152, null);
    	    			}
    	    				
    	    		}
    	    		
    	    		if(highLow == 1)
    	    		{
    	    			for(int v = 0; v < 2; v++)
    	    			{
    	    				if(v == inMenui) g.setColor(Color.red);
    	    				else g.setColor(Color.white);
    	    				g.drawString(onOff[v], 380, 155 + v * 20);
    	    				if(music) g.drawImage(Resources.smallTick, 410, 132, null);
    	    				else if(!music) g.drawImage(Resources.smallTick, 410, 152, null);
    	    			}
    	    		}
    	    		
    	    		if(highLow == 2)
    	    		{
    	    			for(int r = 0; r < 2; r++)
    	    			{
    	    				if(r == inGamei) g.setColor(Color.red);
    	    				else g.setColor(Color.white);
    	    				g.drawString(onOff[r], 545, 155 + r * 20);
    	    				if(PlayState.gamePlayMusic) g.drawImage(Resources.smallTick, 575, 132, null);
    	    				if(!PlayState.gamePlayMusic) g.drawImage(Resources.smallTick, 575, 152, null);
    	    			}
    	    		}
    	    	}
    		  }
    	    }
    	 }
     }
  

  public void onClick(MouseEvent e) {}

  public void onKeyPress(KeyEvent e) {
	  
    if (e.getKeyCode() == KeyEvent.VK_LEFT )
    	{   
           if(help)
        	   {
        	     Resources.back.play();
        	     help = false;
        	   }
    	if(pressedSettings && !powOnOff && !background && !sound)
    		{
    		 Resources.back.play();
    		 pressedSettings = false;
    		}
    	    if(sound && !fx && !inGame && !inMenu)
    	    	{
    	    	 Resources.scroll.play();
    	    	 highLow--;
    	    	}
    	    if(highLow == -1 && sound) sound = false;
    	    if(background)
    	    	{
    	    	 Resources.yesNo.play();
    	    	 blueRed--;
    	    	}
    	    if(pressedStart)
    	    	{
    	    	 pressedStart = false;
    	    	 Resources.back.play();
    	    	}
    	    
    	    if(powOnOff) powOnOff = false;
    	    
    	    if(background && blueRed == -1)
    	    	{
    	    	  background = false;
    	    	  blueRed = 0;
    	    	}
    	  }
    
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
    {  
    	if(sound && !fx && !inGame && !inMenu && highLow !=2)
    		{
    		 Resources.scroll.play();
    		 highLow++;
    		}
    	if(sound && highLow == 3)highLow = 0;
    	if(background && blueRed == 0)
    		{
    		 Resources.yesNo.play();
    		 blueRed++;
    		}
    	if(pressedSettings && currentChoice == 0) background = true;
       
    	else if(pressedSettings && currentChoice == 1)
    	{
    		
    		powOnOff = true;
    		if(!already)
  			{
  			 offOn = 0;
  			 already = true;
  			}
    	}
    	
    	
    	else if(pressedSettings && currentChoice == 2 && !sound)
    	{
    		sound = true;
    		highLow = 0;
    	}
    }

    if(e.getKeyCode() == KeyEvent.VK_M)
    {
    	Resources.back.play();
    	pressedStart = false;
    	pressedSettings = false;
    	help = false;
    	background = false;
    	powOnOff = false;
    	sound = false;
    	fx = false;
    	inMenu = false;
    	inGame = false;
    	
    }
    
    if (e.getKeyCode() == KeyEvent.VK_ENTER)
    {
      Resources.enter.play();
      select();
      
      if(inMenu && inMenui == 1 && music) stopMusic();
      
      else if(inMenu && inMenui == 0)
		try {
			if(!music)player.playMusic(path);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	  
      if(inGame && inGamei == 0) PlayState.gamePlayMusic = true;
      
      else if(inGame && inGamei == 1)PlayState.gamePlayMusic = false;
      
      if(inMenu && inMenui == 0)music = true;
      
      else if(inMenu && inMenui == 1)music = false;
      
      if(fx && fxi == 0)  PlayState.soundFX = true;
      
      else if (fx && fxi == 1) PlayState.soundFX = false;
    	  
      if(powOnOff && offOn == 0)  PlayState.powerUps = true;
      
      if(powOnOff && offOn == 1) PlayState.powerUps = false;
      
      if(background && blueRed == 1 ) PlayState.red = false;
    	 
      if(background && blueRed == 0) PlayState.red = true;
     
      if(pressedSettings && currentChoice == 0)  background = true;
     
     else if(pressedSettings && currentChoice == 1)
  	{
  		powOnOff = true;
  		if(!already)
  			{
  			 offOn = 0;
  			 already = true;
  			}
  	}
      
      if (currentChoice == 0 && !pressedStart && !pressedSettings) pressedStart = true; 
      if(!pressedStart && currentChoice == 3)
    	  {
    	    pressedSettings = true;
    	    currentChoice = 0;
    	  }
      if(!sound && pressedSettings && currentChoice == 2)
    	  {
    	    sound = true;
    	    highLow = 0;
    	  }
   } 
    
    
    if (e.getKeyCode() == KeyEvent.VK_UP) 
    {   
    	if(inMenu && inMenui == 0) inMenu = false;
    	if(fx && fxi == 0) fx = false;
    	if(inGame && inGamei == 0) inGame = false;
    	if(!powOnOff && !sound)
    		{
    		  Resources.scroll.play();
    		  currentChoice--;
    		}
    	if(powOnOff)
    		{
    		  Resources.yesNo.play();
    		  offOn--;
    		}
    	if(fx && fxi == 1)
    		{
    		  Resources.yesNo.play();
    		  fxi--;
    		}
    	
    	if(inGame && inGamei == 1)
    		{
    		  Resources.yesNo.play();
    		  inGamei--;
    		}
    	if(inMenu && inMenui == 1)
    		{ 
    		  Resources.yesNo.play();
    		  inMenui--;
    		}
    	
        if(currentChoice == -1 && !pressedStart && !powOnOff)
        	{
        	   Resources.scroll.play();
        	   currentChoice = options.length - 1;
        	}
        
        if(currentChoice == -1 && pressedStart && !powOnOff)
        	{
        	  Resources.scroll.play();
        	  currentChoice = 2;
        	}
        
        if(background) background = false;
     }
    
    
    if (e.getKeyCode() == KeyEvent.VK_DOWN)
    {
    	
    	if(!powOnOff && !sound)
    		{
    		  Resources.scroll.play();
    		  currentChoice++;
    		}
    	if(powOnOff)
    		{
    		  Resources.yesNo.play();
    		  offOn++;
    		}
    	if(pressedSettings && highLow == 0)fx = true;
    	if(pressedSettings && highLow == 2)inGame = true;
    	if(pressedSettings && highLow == 1)inMenu = true;
    	if(fx && fxi == 0)
    		{
    		  Resources.yesNo.play();
    		  fxi++;
    		}
        if(inMenu && inMenui == 0)
        	{
        	  Resources.yesNo.play();
        	  inMenui++;
        	}
    	if(inGame && inGamei == 0)
    		{
    		  Resources.yesNo.play();
    		  inGamei++;
    		}
     
      if (currentChoice == options.length && !pressedStart && !powOnOff )  currentChoice = 0; 
      
      if(currentChoice == 3 && pressedStart) currentChoice = 0;
      
      if(background) background = false;
      if(sound && highLow == 0) fx = true;
   }
  
  
  }
       @Override
       public void onKeyRelease(KeyEvent e) {
	   // TODO Auto-generated method stub
	}






}