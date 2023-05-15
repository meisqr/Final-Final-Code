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
import java.awt.geom.Rectangle2D;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= Constants.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Constants.ObjectConstants.GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == Constants.ObjectConstants.BARREL || objType == Constants.ObjectConstants.BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == Constants.ObjectConstants.CANNON_LEFT || objType == Constants.ObjectConstants.CANNON_RIGHT)
					doAnimation = false;
			}
		}
	}

	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;

		if (objType == Constants.ObjectConstants.BARREL || objType == Constants.ObjectConstants.BOX || objType == Constants.ObjectConstants.CANNON_LEFT || objType == Constants.ObjectConstants.CANNON_RIGHT)
			doAnimation = false;
		else
			doAnimation = true;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getAniTick() {
		return aniTick;
	}

}
