/**
Playing class extends the class "State" and implements the interface "Statemethods". It contains various fields, 
such as Player, LevelManager, EnemyManager, ObjectManager, and different overlays. Its methods include loading levels and 
updating the game state.

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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class Playing extends State implements Statemethods {

	private Player player;
	private Player player1, player2;
	// try and fix and make two instances(?) of players to make it 
	// multiplayer before networking to get and pass the cooordinates
	// etc etc <3
	LevelManager levelManager;
	EnemyManager enemyManager;
	ObjectManager objectManager;
	private GameOverOverlay gameOverOverlay;
	private GameCompletedOverlay gameCompletedOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;

	private boolean paused = false;

	public int xLvlOffset;
	private int leftBorder = (int) (0.25 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.75 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	public BufferedImage backgroundImg;
	public BufferedImage bigCloud;
	public BufferedImage smallCloud;
	private BufferedImage[] questionImgs, exclamationImgs;

	private int[] smallCloudsPos;
	private Random rnd = new Random();

	public boolean gameOver;
	public boolean lvlCompleted;
	public boolean gameCompleted;
	public boolean playerDying;

	private int playerNum;

	public Playing(Game game, int num) {
		super(game);
		playerNum = num;
		
		player1 = new Player(150, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this, 1);
		player2 = new Player(160, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this, 2);

		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		if (playerNum == 1)
			player = player1;
		else if (playerNum == 2)
			player = player2;

		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

	}

	@Override
	public void update() {
		if (lvlCompleted)
			levelCompletedOverlay.update();
		else if (gameCompleted)
			gameCompletedOverlay.update();
		else if (gameOver)
			gameOverOverlay.update();
		else if (playerDying)
			player.update();
		else {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData());
			checkCloseToBorder();
			
		}
	}


	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
	}

	@Override
	public void draw(Graphics g) {
		if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
		else if (gameCompleted)
			gameCompletedOverlay.draw(g);
	}

	public void drawClouds(Graphics g) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud, i * Constants.Environment.BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), Constants.Environment.BIG_CLOUD_WIDTH, Constants.Environment.BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, Constants.Environment.SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], Constants.Environment.SMALL_CLOUD_WIDTH, Constants.Environment.SMALL_CLOUD_HEIGHT, null);
	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		

		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if (e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
	}

	public void mouseDragged(MouseEvent e) {
		//if (!gameOver && !gameCompleted && !lvlCompleted)
			//if (paused)
				//pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mousePressed(e);
		else if (gameCompleted)
			gameCompletedOverlay.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseMoved(e);
	}

	public void setLevelCompleted(boolean levelCompleted) {
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// No more levels
			gameCompleted = true;
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public Player getPlayer() {
		return player;
	}

	public Player getPlayer1(){
		return player1;
	}

	public Player getPlayer2(){
		return player2;
	}
	

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

	public void stopTheGame(boolean willTheGameStop){
		gameCompleted = willTheGameStop;
	}

	public boolean isTheGameRunning(){
		return gameCompleted;
	}

	public GameCompletedOverlay getCompletedOverlay(){
		return gameCompletedOverlay;
	}

	
}
