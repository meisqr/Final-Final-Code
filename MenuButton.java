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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MenuButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = Constants.UI.Buttons.B_WIDTH / 2;
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, Constants.UI.Buttons.B_WIDTH, Constants.UI.Buttons.B_HEIGHT);
	}

	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * Constants.UI.Buttons.B_WIDTH_DEFAULT, rowIndex * Constants.UI.Buttons.B_HEIGHT_DEFAULT, Constants.UI.Buttons.B_WIDTH_DEFAULT, Constants.UI.Buttons.B_HEIGHT_DEFAULT);
	}

	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, Constants.UI.Buttons.B_WIDTH, Constants.UI.Buttons.B_HEIGHT, null);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}
 
	public void applyGamestate() {
		Gamestate.state = state;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	public Gamestate getState() {
		return state;
	}

}
