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

public class Potion extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;

		initHitbox(7, 14);

		xDrawOffset = (int) (3 * Game.SCALE);
		yDrawOffset = (int) (2 * Game.SCALE);

		maxHoverOffset = (int) (10 * Game.SCALE);
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);

		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox.y = y + hoverOffset;
	}
}
