/**
Statemethods interface defines the methods that must be implemented by any class that represents a state in the game. 
These methods include updating and drawing the state, handling mouse and keyboard events, and so on.

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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {
	public void update();

	public void draw(Graphics g);

	public void mouseClicked(MouseEvent e);

	public void mousePressed(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseMoved(MouseEvent e);

	public void keyPressed(KeyEvent e);

	public void keyReleased(KeyEvent e);

}
