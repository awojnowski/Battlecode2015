package tulsi;

import battlecode.common.*;

public class BattleRobot extends Robot {

	public BattleRobot(RobotController robotController) {
		
		super(robotController);
		
		
		
	}
	
	// MARK: Attacking
	
	public void attack() throws GameActionException {
		
		if (!this.robotController.isWeaponReady()) return;
		
		RobotInfo weakestEnemy = this.weakestEnemy();
		if (weakestEnemy != null) {
			
			this.robotController.attackLocation(weakestEnemy.location);
			
		}
		
	}

	// MARK: Enemy Helpers
	
	public RobotInfo[] enemies() {
		
		return this.robotController.senseNearbyRobots(this.type.attackRadiusSquared, this.team.opponent());
		
	}
	
	public RobotInfo weakestEnemy() {
		
		RobotInfo[] enemies = this.enemies();
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
			return chosenEnemy;
			
		}
		return null;
		
	}
	
}
