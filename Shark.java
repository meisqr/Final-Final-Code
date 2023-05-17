/**
Shark class extends the Enemy class and defines the behavior and properties of a shark enemy in a game. 
It has methods for updating the enemy's position and behavior based on the game state and player actions.

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

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, Constants.EnemyConstants.SHARK_WIDTH, Constants.EnemyConstants.SHARK_HEIGHT, Constants.EnemyConstants.SHARK);
		initHitbox(18, 22);
		initAttackBox(20, 20, 20);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case Constants.EnemyConstants.IDLE:
				if (HelpMethods.IsFloor(hitbox, lvlData))
					newState(Constants.EnemyConstants.RUNNING);
				else
					inAir = true;
				break;
			case Constants.EnemyConstants.RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(Constants.EnemyConstants.ATTACK);
				}

				move(lvlData);
				break;
			case Constants.EnemyConstants.ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(lvlData, playing);
				}

				break;
	 		case Constants.EnemyConstants.HIT:
				if (aniIndex <= Constants.EnemyConstants.GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	protected void attackMove(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == Constants.Directions.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (HelpMethods.CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (HelpMethods.IsFloor(hitbox, xSpeed * 4, lvlData)) {
				hitbox.x += xSpeed * 4;
				return;
			}
		newState(Constants.EnemyConstants.IDLE);
	}
}
