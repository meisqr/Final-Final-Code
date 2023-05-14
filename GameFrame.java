
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;


public class GameFrame extends JFrame {
    
    private int width, height;
    private Container contentPane;
    private Player player1, player2;
    private DrawingComponent dc;
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
        createSprites();
        dc = new DrawingComponent();
        contentPane.add(dc);
        contentPane.add(gameCanvas);
        contentPane.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        setUpAnimationTimer();
        setUpKeyListener();
    }

    private void createSprites(){
        if(playerID == 1) {
            player1 = new Player(50,60,10,20, playing,1);
            player2 = new Player(70,60,10,20, playing,2);
        } else{
            player2 = new Player(50,60,10,20, playing,2);
            player1 = new Player(70,60,10,20, playing,1);
        }
    }

    private void setUpAnimationTimer(){
        int interval = 10;
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                double speed = 5;
                if(up){
                    player1.moveV(-speed);
                } else if (down){
                    player1.moveV(speed);
                } else if (left){
                    player1.moveH(-speed);
                } else if (right){
                    player1.moveH(speed);
                }
                dc.repaint();
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
                int keyCode = ke.getKeyCode();

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
                }
            }
        };
        contentPane.addKeyListener(kl);
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

    private class DrawingComponent extends JComponent{
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            player1.render(g2d,1);
            player2.render(g2d,1);
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
