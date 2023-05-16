/**
LevelManager class manages different levels in a game. It loads levels, creates water, draws 
the game screen and updates water animation using Java's Graphics and BufferedImage classes.

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
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

	private Game game;
	private BufferedImage[] levelSprite;
	private BufferedImage[] waterSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0, aniTick, aniIndex;

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		createWater();
		levels = new ArrayList<>();
		buildAllLevels();
	}

	private void createWater() {
		waterSprite = new BufferedImage[5];
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
		waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
	}

	public void loadNextLevel() {
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
		game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();
		for (BufferedImage img : allLevels)
			levels.add(new Level(img));
	}

	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];
		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}

	public void draw(Graphics g, int lvlOffset) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				int x = Game.TILES_SIZE * i - lvlOffset;
				int y = Game.TILES_SIZE * j;
				if (index == 48)
					g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
				else if (index == 49)
					g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
				else
					g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}

	public void update() {
		updateWaterAnimation();
	}

	private void updateWaterAnimation() {
		aniTick++;
		if (aniTick >= 40) {
			aniTick = 0;
			aniIndex++;

			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}

	public int getLevelIndex() {
		return lvlIndex;
	}

	public void setLevelIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}
}
