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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, play;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	private void createButtons() {
		int menuX = (int) (335 * Game.SCALE);
		int playX = (int) (440 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		play = new UrmButton(playX, y, Constants.UI.URMButtons.URM_SIZE, Constants.UI.URMButtons.URM_SIZE, 0);
		menu = new UrmButton(menuX, y, Constants.UI.URMButtons.URM_SIZE, Constants.UI.URMButtons.URM_SIZE, 2);

	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * Game.SCALE);
		imgH = (int) (img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);

	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		menu.draw(g);
		play.draw(g);
	}

	public void update() {
		menu.update();
		play.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);

		if (isIn(menu, e))
			menu.setMouseOver(true);
		else if (isIn(play, e))
			play.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll();
				
			}
		} else if (isIn(play, e))
			if (play.isMousePressed()) {
				playing.resetAll();
			}

		menu.resetBools();
		play.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menu, e))
			menu.setMousePressed(true);
		else if (isIn(play, e))
			play.setMousePressed(true);
	}

}
