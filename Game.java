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

public class Game implements Runnable {

	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	Menu menu;
	GameOptions gameOptions;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	private final boolean SHOW_FPS_UPS = true;

	public Game() {
		System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
		initClasses();
		startGameLoop();
	}

	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU -> menu.update();
		case PLAYING -> playing.update();
		case OPTIONS -> gameOptions.update();
		case QUIT -> System.exit(0);
		}
	}

	@SuppressWarnings("incomplete-switch")
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU -> menu.draw(g);
		case PLAYING -> playing.draw(g);
		case OPTIONS -> gameOptions.draw(g);
		}
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {

				update();
				updates++;
				deltaU--;

			}

			if (deltaF >= 1) {

				//gameCanvas.repaint();
				frames++;
				deltaF--;

			}

			if (SHOW_FPS_UPS)
				if (System.currentTimeMillis() - lastCheck >= 1000) {

					lastCheck = System.currentTimeMillis();
					System.out.println("FPS: " + frames + " | UPS: " + updates);
					frames = 0;
					updates = 0;

				}

		}
	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public GameOptions getGameOptions() {
		return gameOptions;
	}

}
