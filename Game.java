/**
Game class implements the Runnable interface, which allows the class to be executed as a thread. 
The class contains methods for updating and rendering the game, as well as initializing classes for 
the game's menu, gameplay, and game options. It also includes a game loop that continually updates 
and renders the game, while tracking the frames per second (FPS) and updates per second (UPS).

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

public class Game {

	private Playing playing1, playing2;
	Menu menu;
	GameOptions gameOptions;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


	public Game() {
		System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
		initClasses();
	}

	private void initClasses() {
		menu = new Menu(this);
		playing1 = new Playing(this,1);
		playing2 = new Playing(this,2);
		gameOptions = new GameOptions(this);
	}

	public void update() {
		switch (Gamestate.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				playing1.update();
				playing2.update();
				break;
			case OPTIONS:
				gameOptions.update();
				break;
			case QUIT:
				System.exit(0);
		}
	}

	@SuppressWarnings("incomplete-switch")
	public void render(Graphics g) {
		switch (Gamestate.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				playing1.draw(g);
				playing2.draw(g);
				break;
			case OPTIONS:
				gameOptions.draw(g);
				break;
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing1;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}

	
}
