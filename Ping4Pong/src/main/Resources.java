package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Resources {

	public static BufferedImage welcome,iconimage,line,ball,
	grows,shrinks,freezes,invincible,life,rightFrozen,leftFrozen,
	topFrozen,bottomFrozen,brickTopBottom,brickLeft,brickRight,yellowDeadblue,redDeadblue,whiteDeadblue,greenDeadblue,yellowDeadred,redDeadred,
	whiteDeadred,greenDeadred,redBack,redScreenShot,blueScreenShot,arrow,tick,smallTick,settings,screw,help;
	public static AudioClip hit,bounce,brick,freeze,iceBreak,grow,shrink,lifeUp,invincibility,yesNo,scroll,enter,back;
	public static Color blue,yellow,green,red;
	public static Clip menuClip;
	
	
	public static void load() {
		welcome = loadImage("welcomee.png");
		iconimage = loadImage("iconimage.png");
		yellowDeadblue = loadImage("yellowDeadblue.png");
		redDeadblue = loadImage("redDeadblue.png");
		greenDeadblue = loadImage("greenDeadblue.png");
		whiteDeadblue = loadImage("whiteDeadblue.png");
		yellowDeadred = loadImage("yellowDeadred.png");
		redDeadred = loadImage("redDeadred.png");
		greenDeadred = loadImage("greenDeadred.png");
		whiteDeadred = loadImage("whiteDeadred.png");
		grows = loadImage("grows.png");
		shrinks = loadImage("shrinks.png");
		freezes = loadImage("freezes.png");
		life = loadImage("life.png");
		invincible = loadImage("invincible.png");
		rightFrozen = loadImage("rightFrozen.png");
		leftFrozen = loadImage("leftFrozen.png");
		topFrozen = loadImage("topFrozen.png");
		bottomFrozen = loadImage("bottomFrozen.png");
		brickTopBottom = loadImage("brickTopBottom.png");
		brickLeft = loadImage("brickLeft.png");
		brickRight = loadImage("brickRight.png");
		line = loadImage("line.png");
		ball = loadImage("ball.png");
		redBack = loadImage("redBack.PNG");
		redScreenShot = loadImage("redScreenShot.png");
	    blueScreenShot = loadImage("blueScreenShot.png");
	    arrow = loadImage("arrow.png");
	    tick = loadImage("tick.png");
	    smallTick = loadImage("smallTick.png");
	    settings = loadImage("settings.png");
	    screw = loadImage("screw.png");
	    help = loadImage("help.png");
	    
	    
	    back = loadSound("back.wav");
	    yesNo = loadSound("yesNo.wav");
	    scroll = loadSound("scroll.wav");
	    enter = loadSound("enter.wav");
		hit = loadSound("hit.wav");
		bounce = loadSound("bounce.wav");
		brick = loadSound("brick.wav");
		freeze = loadSound("frozen.wav");
		iceBreak = loadSound("iceBreak.wav");
		shrink = loadSound("shrink.wav");
		grow = loadSound("grow.wav");
		lifeUp = loadSound("life++.wav");
		invincibility = loadSound("invincible.wav");
		
		
		blue = new Color(47, 66, 131); // Constructor accepts RGB
		yellow = new Color(198, 198, 0);
		green = new Color(0, 113, 56);
		red = new Color(204, 0, 12);
	}
	
	private static AudioClip loadSound(String filename) {
		
		URL fileURL = Resources.class.getResource("/resources/" + filename);
		return Applet.newAudioClip(fileURL);
	}

	private static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Resources.class.getResourceAsStream("/resources/" + filename));
		} catch (IOException e) {
			System.out.println("Error while reading: " + filename);
			e.printStackTrace();
		}
		return img;
	}
	
	
	public void playMusic(String path) throws LineUnavailableException
	{
		menuClip = AudioSystem.getClip();
        AudioInputStream inputStream = null;
		try {
			inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(path));
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuClip = AudioSystem.getClip();
        try {
        	menuClip.open(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        menuClip.start(); 
        menuClip.loop(menuClip.LOOP_CONTINUOUSLY);
    } 
 }