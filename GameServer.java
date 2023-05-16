/**
Game server class can handle connections from up to two players. The server listens on a 
port and waits for players to connect. When two players have connected, the server creates 
threads to handle communication with each player. The class includes inner classes ReadFromClient 
and WriteToClient, which implement the logic for reading and writing data to and from the clients.

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

import java.io.*;
import java.net.*;
public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2sSocket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    private float p1x, p1y, p2x, p2y, cx, cy;


    public GameServer(){
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;
        maxPlayers = 2;

        try{
            ss = new ServerSocket(55555);
        } catch (IOException ex){
            System.out.println("IOException from src.main.GameServer constructor");
        }
    }

    public void acceptConnections(){
        try{
            System.out.println("Waiting for connections...");

            while(numPlayers < maxPlayers){
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if(numPlayers == 1){
                    p1Socket = s;
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;
                } else {
                    p2sSocket = s;
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();
                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();
                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    writeThread1.start();
                    writeThread2.start();
                }
            }
            System.out.println("No longer accepting connections");

        } catch(IOException ex){
            System.out.println("IOException from acceptConnections()");
        }
    }

    private class ReadFromClient implements Runnable{

        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int pid, DataInputStream in){
            playerID = pid;
            dataIn = in;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        public void run(){
            try{
                while(true){
                    //dagdagcrab
                    if(playerID == 1){
                        p1x = dataIn.readFloat();
                        p1y = dataIn.readFloat();
                        
                    } else if (playerID == 2) {
                        p2x = dataIn.readFloat();
                        p2y = dataIn.readFloat();
                    }
                }
            } catch (IOException ex){
                System.out.println("IOException from RFC run()");
            }
        }
    }

    private class WriteToClient implements Runnable{

        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int pid, DataOutputStream out){
            playerID = pid;
            dataOut = out;
            System.out.println("WTC" + playerID + " Runnable created");
        }

        public void run(){
            try{
                while(true){
                    if(playerID == 1){
                        dataOut.writeFloat(p2x);
                        dataOut.writeFloat(p2y);
                        //crab
                        dataOut.flush();
                    } else if (playerID == 2) {
                        dataOut.writeFloat(p1x);
                        dataOut.writeFloat(p1y);
                        //crab
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        System.out.println("InterrupedException from WTC run()");
                    }
                }
            } catch (IOException ex){
                System.out.println("IOException from WTC run()");
            }
        }

        public void sendStartMsg(){
            try{
                dataOut.writeUTF("We now have 2 players. Go!");
            } catch(IOException ex){
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }

    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
