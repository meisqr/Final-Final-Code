/**
Gamestate is an enumeration class with 5 predefined values representing the possible states of a game. 
The "state" variable is static and can be accessed and changed from any part of the code.

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

public enum Gamestate {

	PLAYING, MENU, OPTIONS, QUIT, CREDITS;

	public static Gamestate state = MENU;

}
