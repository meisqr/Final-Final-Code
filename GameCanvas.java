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
import java.util.*;
import javax.swing.*;

public class GameCanvas extends JComponent{
    private int width, height;
    private Player player1, player2;
    private Playing playing;
    public Game game;

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        game = new Game();
        playing = new Playing(game);
        player1 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), playing,1);
        player2 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), playing,2);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g.drawImage(playing.backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        
        playing.drawClouds(g);
        
        playing.levelManager.draw(g, playing.xLvlOffset);
        playing.objectManager.draw(g, playing.xLvlOffset);
        playing.enemyManager.draw(g, playing.xLvlOffset);

        player1.render(g2d, playing.xLvlOffset);
        player2.render(g2d, playing.xLvlOffset);

        playing.objectManager.drawBackgroundTrees(g, playing.xLvlOffset);
        playing.drawDialogue(g, playing.xLvlOffset);

        game.render(g2d);
        player1.update();
        
    }
    /*
    public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU -> game.menu.draw(g);
		case PLAYING -> playing.draw(g);
		case OPTIONS -> game.gameOptions.draw(g);
        case CREDITS -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
        case QUIT -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
        default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
		}
	}*/


    public Game getGame(){
        return game;
    }
}
