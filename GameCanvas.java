import java.awt.*;
import java.util.*;
import javax.swing.*;

public class GameCanvas extends JComponent{
    private int width, height;
    private Player player1, player2;
    private Playing playing;
    public Game game;
    private Menu menu;
    private GameOptions gameOptions;
    // add components

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        game = new Game();
        playing = new Playing(game);
        menu = new Menu(game);
        gameOptions = new GameOptions(game);
        player1 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), playing,1);
        player2 = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), playing,2);
        // add components
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
        
    }

    public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU -> game.menu.draw(g);
		case PLAYING -> playing.draw(g);
		case OPTIONS -> game.gameOptions.draw(g);
//		case CREDITS -> credits.draw(g);
        case CREDITS -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
        case QUIT -> throw new UnsupportedOperationException("Unimplemented case: " + Gamestate.state);
        default -> throw new IllegalArgumentException("Unexpected value: " + Gamestate.state);
		}
	}


    public Game getGame(){
        return game;
    }

    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying(){
        return playing;
    }

    public GameOptions getGameOptions(){
        return gameOptions;
    }
}
