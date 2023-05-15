/**
The class DialogueEffect is for a speech bubble animation displayed for enemies in the game. 
It has attributes for the position, type of animation, animation index, and active status. 
The class has methods to update the animation, deactivate it, reset its position, and retrieve its attributes.

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

public class DialogueEffect {

	private int x, y, type;
	private int aniIndex, aniTick;
	private boolean active = true;

	public DialogueEffect(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void update() {
		aniTick++;
		if (aniTick >= Constants.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Constants.Dialogue.GetSpriteAmount(type)) {
				active = false;
				aniIndex = 0;
			}
		}
	}

	public void deactive() {
		active = false;
	}

	public void reset(int x, int y) {
		this.x = x;
		this.y = y;
		active = true;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getType() {
		return type;
	}

	public boolean isActive() {
		return active;
	}
}
