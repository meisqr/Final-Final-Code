/**
Enemy is an abstract class which extends another class called Entity. It contains variables 
and methods that are shared among all enemy objects in the game. The class also defines several 
methods for updating the enemy's behavior.

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

import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = Constants.Directions.LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;
	protected int attackBoxOffsetX;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;

		maxHealth = Constants.EnemyConstants.GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
	}

	protected void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	protected void updateAttackBoxFlip() {
		if (walkDir == Constants.Directions.RIGHT)
			attackBox.x = hitbox.x + hitbox.width;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX;

		attackBox.y = hitbox.y;
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
		this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void inAirChecks(int[][] lvlData, Playing playing) {
		if (state != Constants.EnemyConstants.HIT && state != Constants.EnemyConstants.DEAD) {
			updateInAir(lvlData);
			playing.getObjectManager().checkSpikesTouched(this);
			if (HelpMethods.IsEntityInWater(hitbox, lvlData))
				hurt(maxHealth);
		}
	}

	protected void updateInAir(int[][] lvlData) {
		if (HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += Constants.GRAVITY;
		} else {
			inAir = false;
			hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
	}

	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == Constants.Directions.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (HelpMethods.IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}

	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = Constants.Directions.RIGHT;
		else
			walkDir = Constants.Directions.LEFT;
	}

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		if (playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (HelpMethods.IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}

	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		switch (enemyType) {
		case Constants.EnemyConstants.CRABBY -> {
			return absValue <= attackDistance;
		}
		case Constants.EnemyConstants.SHARK -> {
			return absValue <= attackDistance * 2;
		}
		}
		return false;
	}

	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(Constants.EnemyConstants.DEAD);
		else {
			newState(Constants.EnemyConstants.HIT);
			if (walkDir == Constants.Directions.LEFT)
				pushBackDir = Constants.Directions.RIGHT;
			else
				pushBackDir = Constants.Directions.LEFT;
			pushBackOffsetDir = Constants.Directions.UP;
			pushDrawOffset = 0;
		}
	}

	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-Constants.EnemyConstants.GetEnemyDmg(enemyType), this);
		else {
			if (enemyType == Constants.EnemyConstants.SHARK)
				return;
		}
		attackChecked = true;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= Constants.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Constants.EnemyConstants.GetSpriteAmount(enemyType, state)) {
				if (enemyType == Constants.EnemyConstants.CRABBY || enemyType == Constants.EnemyConstants.SHARK) {
					aniIndex = 0;

					switch (state) {
					case Constants.EnemyConstants.ATTACK, Constants.EnemyConstants.HIT -> state = Constants.EnemyConstants.IDLE;
					case Constants.EnemyConstants.DEAD -> active = false;
					}
				} else if (enemyType == Constants.EnemyConstants.PINKSTAR) {
					if (state == Constants.EnemyConstants.ATTACK)
						aniIndex = 3;
					else {
						aniIndex = 0;
						if (state == Constants.EnemyConstants.HIT) {
							state = Constants.EnemyConstants.IDLE;

						} else if (state == Constants.EnemyConstants.DEAD)
							active = false;
					}
				}
			}
		}
	}

	protected void changeWalkDir() {
		if (walkDir == Constants.Directions.LEFT)
			walkDir = Constants.Directions.RIGHT;
		else
			walkDir = Constants.Directions.LEFT;
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(Constants.EnemyConstants.IDLE);
		active = true;
		airSpeed = 0;

		pushDrawOffset = 0;

	}

	public int flipX() {
		if (walkDir == Constants.Directions.RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == Constants.Directions.RIGHT)
			return -1;
		else
			return 1;
	}

	public boolean isActive() {
		return active;
	}

	public float getPushDrawOffset() {
		return pushDrawOffset;
	}

	public float getX(){
		return super.getX();
	}

	public float getY(){
		return super.getY();
	}

	public void setX(float newValue){
		hitbox.x = newValue;
	}

	public void setY(float newValue){
		hitbox.y = newValue;
	}
}
