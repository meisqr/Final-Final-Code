/**
Crabby class extends the Enemy class and has a constructor that sets certain properties and initializes hitbox and attack box. 
It also has an "update" method that calls other methods to update the enemy's behavior, animation, and attack. One of its methods 
has a switch statement that handles the different states of the enemy, such as idle, running, attacking, and being hit.

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

public class Crabby extends Enemy {

	public Crabby(float x, float y) {
		super(x, y, Constants.EnemyConstants.CRABBY_WIDTH, Constants.EnemyConstants.CRABBY_HEIGHT, Constants.EnemyConstants.CRABBY);
		initHitbox(22, 19);
		initAttackBox(82, 19, 30);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			inAirChecks(lvlData, playing);
		} else {
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

				if (inAir)
					playing.addDialogue((int) hitbox.x, (int) hitbox.y, Constants.Dialogue.EXCLAMATION);

				break;
			case Constants.EnemyConstants.ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
				break;
			case Constants.EnemyConstants.HIT:
				if (aniIndex <= Constants.EnemyConstants.GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	public float getX(){
		return super.getX();
	}

	public float getY(){
		return super.getY();
	}

	public void setX(float newValue){
		hitbox.x = newValue;
		System.out.println(hitbox.x);
	}

	public void setY(float newValue){
		hitbox.y = newValue;
	}

}
