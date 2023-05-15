/**
Player class extends from another class called "Entity". It contains various properties and methods related to 
the behavior and appearance of a player character in a 2D platformer game, such as animations, movement, attack, 
health and power bars, and collision detection with the environment.

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
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class Player extends Entity {

	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping / Gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	// StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;

	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;
	private Playing playing;

	private int tileY = 0;

	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;
	private double xCoord, yCoord;
	private int playerNumber;

	public Player(float x, float y, int width, int height, Playing playing, int num) {
		super(x, y, width, height);
		xCoord = (double) this.x;
		yCoord = (double) this.y;
		this.playing = playing;
		playerNumber = num;
		this.state = Constants.PlayerConstants.IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		xCoord = spawn.x;
		yCoord = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
		resetAttackBox();
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();

		if (currentHealth <= 0) {
			if (state != Constants.PlayerConstants.DEAD) {
				state = Constants.PlayerConstants.DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);

				// Check if player died in air
				if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;
					airSpeed = 0;
				}
			} else if (aniIndex == Constants.ObjectConstants.GetSpriteAmount(Constants.PlayerConstants.DEAD) - 1 && aniTick >= Constants.ANI_SPEED - 1) {
				playing.setGameOver(true);
			} else {
				updateAnimationTick();

				// Fall if in air
				if (inAir)
					if (HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
						hitbox.y += airSpeed;
						airSpeed += Constants.GRAVITY;
					} else
						inAir = false;

			}

			return;
		}

		updateAttackBox();

		if (state == Constants.PlayerConstants.HIT) {
			if (aniIndex <= Constants.PlayerConstants.GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, lvlData, 1.25f);
			updatePushBackDrawOffset();
		} else
			updatePos();

		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}

		if (attacking || powerAttackActive)
			checkAttack();

		updateAnimationTick();
		setAnimation();
	}

	private void checkInsideWater() {
		if (HelpMethods.IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
	}

	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;

		if (powerAttackActive)
			attackChecked = false;

		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
	}

	private void setAttackBoxOnRightSide() {
		attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 5);
	}

	private void setAttackBoxOnLeftSide() {
		attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
	}

	private void updateAttackBox() {
		if (right && left) {
			if (flipW == 1) {
				setAttackBoxOnRightSide();
			} else {
				setAttackBoxOnLeftSide();
			}

		} else if (right || (powerAttackActive && flipW == 1))
			setAttackBoxOnRightSide();
		else if (left || (powerAttackActive && flipW == -1))
			setAttackBoxOnLeftSide();

		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset + (int) (pushDrawOffset)), width * flipW, height, null);
//		drawHitbox(g, lvlOffset);
//		drawAttackBox(g, lvlOffset);
		drawUI(g);
	}

	private void drawUI(Graphics g) {
		// Background ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

		// Health bar
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// Power Bar
		g.setColor(Color.yellow);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	public void updateAnimationTick() {
		aniTick++;
		if (aniTick >= Constants.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Constants.PlayerConstants.GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				if (state == Constants.PlayerConstants.HIT) {
					newState(Constants.PlayerConstants.IDLE);
					airSpeed = 0f;
					if (!HelpMethods.IsFloor(hitbox, 0, lvlData))
						inAir = true;
				}
			}
		}
	}

	private void setAnimation() {
		int startAni = state;

		if (state == Constants.PlayerConstants.HIT)
			return;

		if (moving)
			state = Constants.PlayerConstants.RUNNING;
		else
			state = Constants.PlayerConstants.IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = Constants.PlayerConstants.JUMP;
			else
				state = Constants.PlayerConstants.FALLING;
		}

		if (powerAttackActive) {
			state = Constants.PlayerConstants.ATTACK;
			aniIndex = 1;
			aniTick = 0;
			return;
		}

		if (attacking) {
			state = Constants.PlayerConstants.ATTACK;
			if (startAni != Constants.PlayerConstants.ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (startAni != state)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;

		float xSpeed = 0;

		if (left && !right) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right && !left) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (powerAttackActive) {
			if ((!left && !right) || (left && right)) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			xSpeed *= 3;
		}

		if (!inAir)
			if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir && !powerAttackActive) {
			if (HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += Constants.GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else {
			hitbox.x = HelpMethods.GetEntityXPosNextToWall(hitbox, xSpeed);
			if (powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}

	public void changeHealth(int value) {
		if (value < 0) {
			if (state == Constants.PlayerConstants.HIT)
				return;
			else
				newState(Constants.PlayerConstants.HIT);
		}

		currentHealth += value;
		currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
	}

	public void changeHealth(int value, Enemy e) {
		if (state == Constants.PlayerConstants.HIT)
			return;
		changeHealth(value);
		pushBackOffsetDir = Constants.Directions.UP;
		pushDrawOffset = 0;

		if (e.getHitbox().x < hitbox.x)
			pushBackDir = Constants.Directions.RIGHT;
		else
			pushBackDir = Constants.Directions.LEFT;
	}

	public void kill() {
		currentHealth = 0;
	}

	public void changePower(int value) {
		powerValue += value;
		powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
	}

	private void loadAnimations() {
		if (playerNumber == 1){
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS1);
			animations = new BufferedImage[7][8];
			for (int j = 0; j < animations.length; j++)
				for (int i = 0; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
		} else if (playerNumber == 2){
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS2);
			animations = new BufferedImage[7][8];
			for (int j = 0; j < animations.length; j++)
				for (int i = 0; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
		}
		
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = Constants.PlayerConstants.IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		powerValue = powerMaxValue;

		hitbox.x = x;
		hitbox.y = y;
		resetAttackBox();

		if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	private void resetAttackBox() {
		if (flipW == 1)
			setAttackBoxOnRightSide();
		else
			setAttackBoxOnLeftSide();
	}

	public int getTileY() {
		return tileY;
	}

	public void powerAttack() {
		if (powerAttackActive)
			return;
		if (powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}

	}

	public void moveH(double n){
		xCoord += n;
	}

	public void moveV(double n){
		yCoord +=n;
	}

	public void setX(double n){
		xCoord = n;
	}

	public void setY(double n){
		yCoord = n;
	}

	public double getX(){
		return xCoord;
	}

	public double getY(){
		return yCoord;
	}


}
