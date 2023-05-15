/**
GameCanvas class .....
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
    //private DrawingComponent dc;
    private Timer animationTimer;
    private Boolean up, down, left, right;
    private Socket socket;
    private int playerID;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    private String ipAddress;
    private Game newGame;
    private Playing playing;
    private GameCanvas gameCanvas;


    public GameFrame(int w, int h){
        width = w;
        height = h;
        up = false;
        down = false;
        left = false;
        right = false;
        playing = new Playing(newGame);
        gameCanvas = new GameCanvas(w, h);
    }

    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("Player #" + playerID);
        contentPane.setPreferredSize(new Dimension(width, height));
        //createSprites();
        //dc = new DrawingComponent();
        //contentPane.add(dc);
        contentPane.add(gameCanvas);
        contentPane.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
  
        setUpAnimationTimer();
        setUpKeyListener();
        setUpMouseListener();
        setUpMouseMotionListener();
        
        startGameLoop();  
    }

    /*private void createSprites(){
        if(playerID == 1) {
            player1 = new Player(50,60,10,20, playing,1);
            player2 = new Player(70,60,10,20, playing,2);
        } else{
            player2 = new Player(50,60,10,20, playing,2);
            player1 = new Player(70,60,10,20, playing,1);
        }
    }*/

    private void setUpAnimationTimer(){
        int interval = 10;
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                double speed = 5;
                /*if(up){
                    player1.moveV(-speed);
                } else if (down){
                    player1.moveV(speed);
                } else if (left){
                    player1.moveH(-speed);
                } else if (right){
                    player1.moveH(speed);
                }*/
                //dc.repaint();
                gameCanvas.repaint();
            }
        };
        animationTimer = new Timer(interval, al);
        animationTimer.start();
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
                    case QUIT -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
                    }
                int keyCode = ke.getKeyCode();

                switch(keyCode){
                    case KeyEvent.VK_W:
                        up = true;
                        break;
                    case KeyEvent.VK_S:
                        down = true;
                        break;
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_D:
                        right = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                switch (Gamestate.state) {
                    case MENU -> gameCanvas.getGame().getMenu().keyReleased(ke);
                    case PLAYING -> gameCanvas.getGame().getPlaying().keyReleased(ke);
                    case CREDITS -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    case OPTIONS -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    case QUIT -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
                    default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
                    }
                /*int keyCode = ke.getKeyCode();

                switch(keyCode){
                    case KeyEvent.VK_W:
                        up = false;
                        break;
                    case KeyEvent.VK_S:
                        down = false;
                        break;
                    case KeyEvent.VK_A:
                        left = false;
                        break;
                    case KeyEvent.VK_D:
                        right = false;
                        break;
                }*/
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
		case MENU -> gameCanvas.getMenu().update();
		case PLAYING -> gameCanvas.getPlaying().update();
		case OPTIONS -> gameCanvas.getGameOptions().update();
//		case CREDITS -> credits.update();
		case QUIT -> System.exit(0);
		}
	}
    @SuppressWarnings("incomplete-switch")
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU -> gameCanvas.getMenu().draw(g);
		case PLAYING -> gameCanvas.getPlaying().draw(g);
		case OPTIONS -> gameCanvas.getGameOptions().draw(g);
//		case CREDITS -> credits.draw(g);
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

    public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

    public void connectToServer(){
        try{
            socket = new Socket("localhost", 45371);
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
                    double player2X = dataIn.readDouble();
                    double player2Y = dataIn.readDouble();
                    if(player2 != null){
                        player2.setX(player2X);
                        player2.setY(player2Y);
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
                    if(player1 != null){
                        dataOut.writeDouble(player1.getX());
                        dataOut.writeDouble(player1.getY());
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(25);
                    } catch (InterruptedException ex){
                        System.out.println("InterruptedException from WTS run()");
                    }
                }

            } catch(IOException ex){
                System.out.println("IOException from WTS run()");
            }
        }
    }

}
