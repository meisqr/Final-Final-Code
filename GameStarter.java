/**
The GameStarter class is a main method that creates a new GameFrame instance with the dimensions 
specified in the Game class, then calls its connectToServer and setUpGUI methods. This class is used 
to start the game by initializing the graphical user interface and connecting to the game server.

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

public class GameStarter{
    
    public static void main(String[] args){
        GameFrame gf = new GameFrame(Game.GAME_WIDTH,Game.GAME_HEIGHT);
        gf.connectToServer();
        gf.setUpGUI();
        
    }
}
