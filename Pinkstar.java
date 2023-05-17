/**
Pinkstar class extends another class called "Enemy". It has several fields and methods that define its 
behavior, such as updating its behavior, checking for damage to the player, setting its walk direction, and moving.

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

public class Pinkstar extends Enemy {

	private boolean preRoll = true;
	private int tickSinceLastDmgToPlayer;
	private int tickAfterRollInIdle;
	private int rollDurationTick, rollDuration = 300;

	public Pinkstar(float x, float y) {
		super(x, y, Constants.EnemyConstants.PINKSTAR_WIDTH, Constants.EnemyConstants.PINKSTAR_HEIGHT, Constants.EnemyConstants.PINKSTAR);
		initHitbox(17, 21);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case Constants.EnemyConstants.IDLE:
				preRoll = true;
				if (tickAfterRollInIdle >= 120) {
					if (HelpMethods.IsFloor(hitbox, lvlData))
						newState(Constants.EnemyConstants.RUNNING);
					else
						inAir = true;
					tickAfterRollInIdle = 0;
					tickSinceLastDmgToPlayer = 60;
				} else
					tickAfterRollInIdle++;
				break;
			case Constants.EnemyConstants.RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					newState(Constants.EnemyConstants.ATTACK);
					setWalkDir(playing.getPlayer());
				}
				move(lvlData, playing);
				break;
			case Constants.EnemyConstants.ATTACK:
				if (preRoll) {
					if (aniIndex >= 3)
						preRoll = false;
				} else {
					move(lvlData, playing);
					checkDmgToPlayer(playing.getPlayer());
					checkRollOver(playing);
				}
				break;
			case Constants.EnemyConstants.HIT:
				if (aniIndex <= Constants.EnemyConstants.GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				tickAfterRollInIdle = 120;

				break;
			}
		}
	}

	private void checkDmgToPlayer(Player player) {
		if (hitbox.intersects(player.getHitbox()))
			if (tickSinceLastDmgToPlayer >= 60) {
				tickSinceLastDmgToPlayer = 0;
				player.changeHealth(-Constants.EnemyConstants.GetEnemyDmg(enemyType), this);
			} else
				tickSinceLastDmgToPlayer++;
	}

	private void setWalkDir(Player player) {
		if (player.getHitbox().x > hitbox.x)
			walkDir = Constants.Directions.RIGHT;
		else
			walkDir = Constants.Directions.LEFT;

	}

	protected void move(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == Constants.Directions.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (state == Constants.EnemyConstants.ATTACK)
			xSpeed *= 2;

		if (HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (HelpMethods.IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		if (state == Constants.EnemyConstants.ATTACK) {
			rollOver(playing);
			rollDurationTick = 0;
		}

		changeWalkDir();

	}

	private void checkRollOver(Playing playing) {
		rollDurationTick++;
		if (rollDurationTick >= rollDuration) {
			rollOver(playing);
			rollDurationTick = 0;
		}
	}

	private void rollOver(Playing playing) {
		newState(Constants.EnemyConstants.IDLE);
	}

}
