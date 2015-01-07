package tulsi;

import battlecode.common.*;

public enum BattleRobotAttackStyle {
	STOP_ON_ATTACK,
	STRAFE_ON_ATTACK
}

public class BattleRobot extends Robot {

	public BattleRobotAttackStyle attackStyle;
	public Boolean canBeMobilized;
	
	public BattleRobot(RobotController robotController) {
		
		super(robotController);
		
		this.attackStyle = BattleRobotAttackStyle.STOP_ON_ATTACK;
		this.canBeMobilized = true;
		
	}
	
	// MARK: Attacking
	
	public Boolean attack() throws GameActionException {
		
		if (!this.robotController.isWeaponReady()) return false;
		
		RobotInfo desiredEnemy = this.desiredEnemy(this.enemies());
		if (desiredEnemy != null) {
			
			this.robotController.attackLocation(desiredEnemy.location);
			
			if (this.attackStyle == BattleRobotAttackStyle.STOP_ON_ATTACK) {
				
				this.stopFor(this.type.attackDelay);
				
			}
			
			return true;
			
		}
		return false;
		
	}

	// MARK: Enemy Helpers
	
	public RobotInfo[] enemies() {
		
		return this.robotController.senseNearbyRobots(this.type.attackRadiusSquared, this.team.opponent());
		
	}
	
	public RobotInfo desiredEnemy(RobotInfo[] enemies) {
		
		// prioritize towers
		
		for (RobotInfo enemy : enemies) {
			
			if (enemy.type == RobotType.TOWER) return enemy;
			
		}
		
		// otherwise just the weakest enemy
		
		return weakestEnemy(enemies);
		
	}
	
	public RobotInfo weakestEnemy(RobotInfo[] enemies) {
		
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
	
	// MARK: Mobilization
	
	public Boolean shouldMobalize() {
		
		if (!this.canBeMobilized) return false;
		if (!this.shouldMove()) return false;
		return Clock.getRoundNum() > 500;
		
	}
	
	public void mobalize() throws GameActionException {
		
		if (!this.canBeMobilized) return;
		
		this.moveToward(this.closestEnemyTower());
		
	}
	
}
