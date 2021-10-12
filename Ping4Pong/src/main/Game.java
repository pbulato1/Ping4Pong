package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import framework.InputHandler;
import state.LoadState;
import state.PlayState;
import state.State;
//test

//testing
@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable {
	private int gameWidth;
	private int gameHeight;
	public static Image gameImage;
    private Thread gameThread;
	private volatile boolean running;
	private volatile State currentState;
	public static boolean isPaused;
    private InputHandler inputHandler;

	public Game(int gameWidth, int gameHeight) {
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		setPreferredSize(new Dimension(gameWidth, gameHeight));
		setBackground(Color.BLACK);
		setFocusable(true);
		requestFocus();
	}
	
	

	public void setCurrentState(State newState) {
		System.gc();
		newState.init();
		currentState = newState;
		inputHandler.setCurrentState(currentState);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		initInput();
		setCurrentState(new LoadState());
		initGame();
	}

	private void initGame() {
		running = true;
		gameThread = new Thread(this, "Game Thread");
		gameThread.start();
	}

	public void pauseGame()
	{
		isPaused = true;
	}
	
	public synchronized void resumeGame() 
	{  
		isPaused = false;
		notify(); 
	}

	@Override
	public void run() {
		
		while (running) {
			
			try {
				if (isPaused) 
				{
					synchronized(this)
					{ 
						while (isPaused && running) 
                         wait();
					}
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			prepareGameImage();
			currentState.render(gameImage.getGraphics());
			currentState.update();
			repaint();
			
			try {
				Thread.sleep(12);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}

	private void prepareGameImage() {
		if (gameImage == null) {
			gameImage = createImage(gameWidth, gameHeight);
		}
		Graphics g = gameImage.getGraphics();
		g.clearRect(0, 0, gameWidth, gameHeight);
	}

	public void exit() {
		running = false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (gameImage == null) {
			return;
		}
		g.drawImage(gameImage, 0, 0, null);
	}

	private void initInput() {
		inputHandler = new InputHandler();
		addKeyListener(inputHandler);
		addMouseListener(inputHandler);
    }
}