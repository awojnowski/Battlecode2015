package team170;

import battlecode.common.*;

public class BattleRobot extends Robot {

	public Boolean canBeMobilized;
	public Boolean isMobilized;
	
	public BattleRobot(RobotController robotController) {
		
		super(robotController);
		
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
	
	class AttackResult {
		public RobotInfo target;
	}
	
	public Boolean attack() throws GameActionException {
		
		return this.attack(null);
		
	}
	
	public Boolean attack(AttackResult attackResult) throws GameActionException {
		
		if (!this.robotController.isWeaponReady()) return true;
		
		RobotInfo desiredEnemy = this.desiredEnemy(this.unitController.nearbyAttackableEnemies());
		if (desiredEnemy != null) {
			
			this.robotController.attackLocation(desiredEnemy.location);
			if (attackResult != null) {
				
				attackResult.target = desiredEnemy;
				
			}
			return true;
			
		}
		return false;
		
	}

	// MARK: Enemy Helpers
	
	public RobotInfo desiredEnemy(RobotInfo[] enemies) throws GameActionException {
		
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
	
	public RobotInfo weakestEnemy(RobotInfo[] enemies) throws GameActionException {
		
		if (enemies.length > 0) {
							
			RobotInfo chosenEnemy = null;
			for (RobotInfo enemy : enemies) {
				
				// first report launcher sightings
				this.broadcaster.evaluateSeenLaunchersWithType(enemy.type);

				// restrictions on units
				if (enemy.type == Missile.type()) continue;
				if (this.type == Drone.type() && enemy.type != Beaver.type() && enemy.type != Miner.type()) continue;
				
				// figure out the best enemy
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
	
	public RobotInfo nearbyDangerousEnemy() throws GameActionException {
		
		RobotInfo dangerousEnemy = null;
		
		MapLocation currentLocation = this.locationController.currentLocation();
		RobotInfo[] enemies = this.unitController.nearbyEnemies(16);
		for (RobotInfo enemy : enemies) {
			
			this.broadcaster.evaluateSeenLaunchersWithType(enemy.type);
			if (currentLocation.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared) {
				
				dangerousEnemy = enemy;
				break;
				
			}
			
		}
		return dangerousEnemy;
		
	}
	
	// MARK: Mobilization
	
	public Boolean shouldMobilize() throws GameActionException {
		
		if (this.isMobilized) return true; // if we are mobilized, we are attacking yo
		if (!this.canBeMobilized) return false;

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
