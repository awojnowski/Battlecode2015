package tulsi;

import battlecode.common.*;

public class Soldier extends BattleRobot {

	public Soldier(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			RobotInfo[] enemies = this.robotController.senseNearbyRobots(this.robotController.getType().attackRadiusSquared, this.robotController.getTeam().opponent());
			if (enemies.length > 0) {
								
				RobotInfo chosenEnemy = null;
				for (RobotInfo enemy : enemies) {
					
					if (chosenEnemy == null) {
						
						chosenEnemy = enemy;
						
					} else {
						
						if (chosenEnemy.health > enemy.health) {
							
							chosenEnemy = enemy;
							
						}
						
					}
					
				}
				this.robotController.attackLocation(chosenEnemy.location);
				
			}
			
			this.moveTo(this.randomDirection());
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 60;
	}
	
	public static RobotType type() {
		return RobotType.SOLDIER;
	}

}
