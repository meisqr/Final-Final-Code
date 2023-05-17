/**
GameCompletedOverlay class creates a game overlay that is shown when the game is completed. 
It includes a background image, two menu buttons ("quit" and "credit"), and methods for handling 
mouse events. The class is dependent on other classes and methods for loading images, defining 
button behavior, and updating game state.

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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameCompletedOverlay {
	private Playing playing;
	private BufferedImage img;
	private MenuButton quit;
	private int imgX, imgY, imgW, imgH;
	private String resultImage;

	public GameCompletedOverlay(Playing playing) {
		this.playing = playing;
		resultImage = LoadSave.GAME_COMPLETED;
		createImg();
		createButtons();
	}

	private void createButtons() {
		quit = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 2, Gamestate.MENU);
	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(resultImage);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);

	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		quit.draw(g);
	}

	public void update() {
		quit.update();
	}

	private boolean isIn(MenuButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		quit.setMouseOver(false);

		if (isIn(quit, e))
			quit.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(quit, e)) {
			if (quit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				System.exit(0);
			}
		}

		quit.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(quit, e))
			quit.setMousePressed(true);
	}


	public void determineWinner(boolean result){
		if (result == true)
			resultImage = LoadSave.GAME_COMPLETED;
		//else
			//resultImage = LoadSave.GAME_COMPLETED2;
	}
}
