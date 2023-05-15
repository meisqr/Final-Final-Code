/**
GameCanvas class extends JComponent and is responsible for painting the game graphics. 
It also creates instances of the Player, Playing, and Game classes and uses their methods to draw the game's background, 
levels, objects, enemies, players, and dialogue. The GameCanvas also updates the game and player positions.

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
import java.util.*;
import javax.swing.*;

public class GameCanvas extends JComponent{
    private int width, height;
    private Player player1, player2;
    public Game game;

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        game = new Game();
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g.drawImage(game.getPlaying().backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        
        game.getPlaying().drawClouds(g);
        
        game.getPlaying().levelManager.draw(g, game.getPlaying().xLvlOffset);
        game.getPlaying().objectManager.draw(g, game.getPlaying().xLvlOffset);
        game.getPlaying().objectManager.drawBackgroundTrees(g2d, game.getPlaying().xLvlOffset);
        game.getPlaying().enemyManager.draw(g, game.getPlaying().xLvlOffset);

        game.getPlaying().getPlayer().render(g2d, game.getPlaying().xLvlOffset);
        

        game.getPlaying().objectManager.drawBackgroundTrees(g, game.getPlaying().xLvlOffset);
        game.getPlaying().drawDialogue(g, game.getPlaying().xLvlOffset);

        game.render(g2d);
        game.getPlaying().update();
        
        
    }


    public Game getGame(){
        return game;
    }
}
