/**
BackgroundTree is a class that represents a tree object with properties such as x-coordinate, y-coordinate, type, and animation index. 
The constructor initializes these properties with input values and sets a random animation index value. 
The class also has update and getter/setter methods for these properties. 
The update method updates the animation index property after a certain interval.

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

import java.util.Random;

public class BackgroundTree {

	private int x, y, type, aniIndex, aniTick;

	public BackgroundTree(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;

		// Sets the aniIndex to a random value, to get some variations for the trees so
		// they all don't move in synch.
		Random r = new Random();
		aniIndex = r.nextInt(4);

	}

	public void update() {
		aniTick++;
		if (aniTick >= 35) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public void setAniIndex(int aniIndex) {
		this.aniIndex = aniIndex;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
