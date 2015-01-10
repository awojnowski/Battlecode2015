package boyer11540110;

import battlecode.common.*;

enum BattleRobotAttackStyle {
	STOP_ON_ATTACK,
	STRAFE_ON_ATTACK
}

public class BattleRobot extends Robot {

	public BattleRobotAttackStyle attackStyle;
	public Boolean canBeMobilized;
	public Boolean isMobilized;
	
	public BattleRobot(RobotController robotController) {
		
		super(robotController);
		
		this.attackStyle = BattleRobotAttackStyle.STOP_ON_ATTACK;
		this.canBeMobilized = true;
		this.isMobilized = false;
		
	}
	
	public void run() {
		
		super.run();
		
		try {
			Boolean shouldMobilize = this.shouldMobilize();
			if (!this.isMobilized && shouldMobilize) {
				this.isMobilized = true;
			}
		}
		catch (GameActionException e) {}
		
	}
	
	// MARK: Attacking
	
	public Boolean attack() throws GameActionException {
		
		if (!this.robotController.isWeaponReady()) return false;
		
		RobotInfo desiredEnemy = this.desiredEnemy(this.unitController.nearbyAttackableEnemies());
		if (desiredEnemy != null) {
			
			this.robotController.attackLocation(desiredEnemy.location);
			
			if (this.attackStyle == BattleRobotAttackStyle.STOP_ON_ATTACK) {
				
				this.movementController.stopFor(this.type.attackDelay);
				
			}
			
			return true;
			
		}
		return false;
		
	}

	// MARK: Enemy Helpers
	
	public RobotInfo desiredEnemy(RobotInfo[] enemies) {
		
		// prioritize the HQ
		
		for (RobotInfo enemy : enemies) {
			
			if (enemy.type == RobotType.HQ) return enemy;
			
		}
		
		// prioritize towers next
		
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
					
					if (enemy.type == Missile.type()) continue;
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
	
	public Boolean shouldMobilize() throws GameActionException {
		
		if (this.isMobilized) return true; // if we are mobilized, we are attacking yo
		
		if (!this.canBeMobilized) return false;
		if (!this.movementController.shouldMove()) return false;

		int roundNum = Clock.getRoundNum();
		return this.currentPlaystyle().canMobilizeForClockNumber(roundNum);
		
	}
	
	public void mobilize() throws GameActionException {
		
		if (!this.canBeMobilized) return;
		
		MapLocation objective = this.locationController.bestObjective();
		if (objective != null) {

			this.movementController.moveToward(objective);
			
		}
		
	}
	
}
