/**
LoadSave class contains static methods for loading and saving images in a game. It has 
methods for getting sprite atlases, loading levels, and getting individual images from files. 
The class also contains a list of file names for different game assets.

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;

public class LoadSave {

	public static final String PLAYER_ATLAS1 = "player_sprites.png";
	public static final String PLAYER_ATLAS2 = "player_sprites2.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
//	public static final String MENU_BACKGROUND = "menu_background.png";
	//public static final String PAUSE_BACKGROUND = "pause_menu.png";
	//public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	//public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String BIG_CLOUDS = "big_clouds.png";
	public static final String SMALL_CLOUDS = "small_clouds.png";
	public static final String CRABBY_SPRITE = "crabby_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String COMPLETED_IMG = "completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANNON_ATLAS = "cannon_atlas.png";
	public static final String CANNON_BALL = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	//public static final String OPTIONS_MENU = "options_background.png";
	public static final String PINKSTAR_ATLAS = "pinkstar_atlas.png";
	public static final String QUESTION_ATLAS = "question_atlas.png";
	public static final String EXCLAMATION_ATLAS = "exclamation_atlas.png";
	public static final String SHARK_ATLAS = "shark_atlas.png";
	public static final String GRASS_ATLAS = "grass_atlas.png";
	public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
	public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
	public static final String GAME_COMPLETED = "game_completed.png";
	public static final String WATER_TOP = "water_atlas_animation.png";
	public static final String WATER_BOTTOM = "water.png";
	
	private static ArrayList<String> levelFiles;

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream(fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	public static BufferedImage[] GetAllLevels() {
		levelFiles = new ArrayList<String>();
		for(int i = 1; i <= 5; i++){
			levelFiles.add("" + i + ".png");
		}
		
		File file = null;
		File[] files = new File[levelFiles.size()];

		try {
			for(int i = 0, n = 1; i < levelFiles.size() ; i++, n++){
				URL url = LoadSave.class.getResource("" + n + ".png");
				file = new File (url.toURI());
				files[i] = file;
			}
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException from GetAllLevels()");
		}

		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];

			}

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}



}
