/**
GameFrame is a class that extends the JFrame class and implements the Runnable interface. 
It sets up a GUI for a game, and contains variables and methods to control the game loop, 
animation timer, and player movements. The class also has variables for the game canvas, player 
IDs, IP addresses, and sockets to connect to a server.

@author Jervie S. Manabat (223961), Giuliana Patricia Gabriele L. Bautista (220811)
@version May 15,2023
**/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;


public class GameFrame extends JFrame implements Runnable {
    private Thread gameThread;
    private final int FPS_SET = 120;
	private final int UPS_SET = 200;
    public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	private final boolean SHOW_FPS_UPS = true;
    private int width, height;
    private Container contentPane;
    private Player player1, player2;
    private Socket socket;
    public static int playerID;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    private GameCanvas gameCanvas;
    private Playing isTheGameRunning;



    public GameFrame(int w, int h){
        width = w;
        height = h;
        gameCanvas = new GameCanvas(w, h);
    }

    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("Player #" + playerID);
        contentPane.setPreferredSize(new Dimension(width, height));
        createSprites();
        contentPane.add(gameCanvas);
        contentPane.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        startGameLoop();  
        setUpKeyListener();
        setUpMouseListener();
        setUpMouseMotionListener();
        
    }

    private void createSprites(){
        isTheGameRunning = gameCanvas.getGame().getPlaying();
        isTheGameRunning.resetGameCompleted(); // to make sure it is false at the start
        if(playerID == 1) {
            player1 = gameCanvas.getPlayer1();
            player2 = gameCanvas.getPlayer2();
        } else{
            player2 = gameCanvas.getPlayer1();
            player1 = gameCanvas.getPlayer2();
        }
    }

    public void setUpKeyListener(){
        KeyListener kl = new KeyListener(){

            @Override
            public void keyTyped(KeyEvent ke) {

            }

            @Override
            public void keyPressed(KeyEvent ke) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().keyPressed(ke);
                    case PLAYING -> gameCanvas.getGame().getPlaying().keyPressed(ke);
                    case OPTIONS -> gameCanvas.getGame().getGameOptions().keyPressed(ke);
                    case CREDITS -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    case QUIT -> System.exit(0);
                    default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
                    }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().keyReleased(ke);
                    case PLAYING -> gameCanvas.getGame().getPlaying().keyReleased(ke);
                    case CREDITS, OPTIONS, QUIT -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
                    }
            }
        };
        contentPane.addKeyListener(kl);
    }

    public void setUpMouseListener(){
        MouseListener ml = new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (Gamestate.state) {
                    case PLAYING -> gameCanvas.getGame().getPlaying().mouseClicked(e);
                    }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().mousePressed(e);
                    case PLAYING -> gameCanvas.getGame().getPlaying().mousePressed(e);
                    case OPTIONS -> gameCanvas.getGame().getGameOptions().mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().mouseReleased(e);
                    case PLAYING -> gameCanvas.getGame().getPlaying().mouseReleased(e);
                    case OPTIONS -> gameCanvas.getGame().getGameOptions().mouseReleased(e);
                }    
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // not used
            }
        };
        contentPane.addMouseListener(ml);
    }

    public void setUpMouseMotionListener(){
        MouseMotionListener mml = new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                switch (Gamestate.state) {
                    case PLAYING -> gameCanvas.getGame().getPlaying().mouseDragged(e);
                    case OPTIONS -> gameCanvas.getGame().getGameOptions().mouseDragged(e);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().mouseMoved(e);
                    case PLAYING -> gameCanvas.getGame().getPlaying().mouseMoved(e);
                    case OPTIONS -> gameCanvas.getGame().getGameOptions().mouseMoved(e);
                    }
            }
        };
        contentPane.addMouseMotionListener(mml);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
		switch (Gamestate.state) {
		case MENU -> gameCanvas.getGame().getMenu().update();
		case PLAYING -> gameCanvas.getGame().getPlaying().update();
		case OPTIONS -> gameCanvas.getGame().getGameOptions().update();
		case QUIT -> System.exit(0);
		}
	}
    @SuppressWarnings("incomplete-switch")
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU -> gameCanvas.getGame().getMenu().draw(g);
		case PLAYING -> gameCanvas.getGame().getPlaying().draw(g);
		case OPTIONS -> gameCanvas.getGame().getGameOptions().draw(g);
		}
	}
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {

				update();
				updates++;
				deltaU--;

			}

			if (deltaF >= 1) {

				gameCanvas.repaint();
				frames++;
				deltaF--;

			}

			if (SHOW_FPS_UPS)
				if (System.currentTimeMillis() - lastCheck >= 1000) {

					lastCheck = System.currentTimeMillis();
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					frames = 0;
					updates = 0;

				}

		}
	}

    public void connectToServer(String ipAddress){
        try{
            socket = new Socket(ipAddress, 55555);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are player #" + playerID);
            if(playerID == 1){
                System.out.println("Waiting for Player #2 to connect...");
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
        } catch(IOException ex){
            System.out.println("IOException from connectToServer()");
        }
    }


    private class ReadFromServer implements Runnable{

        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable created");
        }

        public void run(){
            try{
                while(true){
                    
			        if(playerID == 1) {
						float player2X = dataIn.readFloat();
						float player2Y = dataIn.readFloat();
                        boolean isTheGameRunningBool = dataIn.readBoolean();
						if (player2 != null) {
							player2.setX(player2X);
							player2.setY(player2Y);
                            isTheGameRunning.stopTheGame(isTheGameRunningBool);
                            isTheGameRunning.getCompletedOverlay().determineWinner(isTheGameRunningBool);
                            if (isTheGameRunningBool == true){
                                isTheGameRunning.getCompletedOverlay().determineWinner(isTheGameRunningBool);
                                 // get the losing thing
                                //System.exit(0); // reflects on player1 if player2 finishes first
                            }
						}
                    }else if (playerID == 2){
						float player1X = dataIn.readFloat();
						float player1Y = dataIn.readFloat();
                        boolean isTheGameRunningBool = dataIn.readBoolean();
						if(player1 != null){
							player1.setX(player1X);
							player1.setY(player1Y);
                            isTheGameRunning.stopTheGame(isTheGameRunningBool);
                            isTheGameRunning.getCompletedOverlay().determineWinner(isTheGameRunningBool);
                            if (isTheGameRunningBool == true)
                                isTheGameRunning.getCompletedOverlay().determineWinner(isTheGameRunningBool);
                                //System.exit(0); // exits player2 if player1 wins the game
		  	           }
                    }
				}
            } catch(IOException ex){
                System.out.println("IOException from RFS run()");
            }
        }

        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
            } catch(IOException ex){
                System.out.println("IOException from waitForStartMSg()");
            }
        }
    }

    private class WriteToServer implements Runnable{

        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out){
            dataOut = out;
            System.out.println("WTS Runnable created");
        }

        public void run(){
            try{

                while(true){
                    if (player1 != null){
                        if(playerID == 1){
                            dataOut.writeFloat(player1.getX());
                            dataOut.writeFloat(player1.getY());
                            dataOut.writeBoolean(isTheGameRunning.isTheGameRunning());
                            dataOut.flush();
                        }
                        else if(player2 != null && playerID == 2){
                            dataOut.writeFloat(player2.getX());
                            dataOut.writeFloat(player2.getY());
                            dataOut.writeBoolean(isTheGameRunning.isTheGameRunning());
                            dataOut.flush();
                        }
                    }
                    
                    if (player1 != null){
                        try{
                            Thread.sleep(25);
                        } catch (InterruptedException ex){
                            System.out.println("InterruptedException from WTS run()");
                        }
                    }
                    
                }

            } catch(IOException ex){
                System.out.println("IOException from WTS run()");
            }
        }
    }

}
