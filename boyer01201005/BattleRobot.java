package boyer01201005;

import java.util.ArrayList;

import boyer01201005.units.UnitController;

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
		
		if (enemies.length == 0) return null;
		
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
				
				// figure out the best enemy
				if (chosenEnemy == null) {
					
					chosenEnemy = enemy;
					
				} else {
					
					if (enemy.type == Missile.type() && chosenEnemy.type == Missile.type() ||
						enemy.type != Missile.type()) {
						
						if (chosenEnemy.health > enemy.health) {
							
							chosenEnemy = enemy;
							
						}
						
					}
					
				}
				
			}
			return chosenEnemy;
			
		}
		return null;
		
	}
	
	public RobotInfo[] nearbyDangerousEnemies() throws GameActionException {
		
		return this.nearbyDangerousEnemies(16, 0);
		
	}
	
	public RobotInfo[] nearbyDangerousEnemies(int visionRadius, double attackRadius) throws GameActionException {
		
		ArrayList<RobotInfo> dangerousEnemies = new ArrayList<RobotInfo>();
		
		MapLocation currentLocation = this.locationController.currentLocation();
		RobotInfo[] enemies = this.unitController.nearbyEnemies(visionRadius);
		for (RobotInfo enemy : enemies) {
			
			this.broadcaster.evaluateSeenLaunchersWithType(enemy.type);
			if (!UnitController.isUnitTypeMilitary(enemy.type)) continue;
			if (currentLocation.distanceSquaredTo(enemy.location) <= (attackRadius > 0 ? attackRadius : enemy.type.attackRadiusSquared)) {

				dangerousEnemies.add(enemy);
				
			}
			
		}
		return dangerousEnemies.toArray(new RobotInfo[dangerousEnemies.size()]);
		
	}
	
	public RobotInfo[] enemiesInTerritory() throws GameActionException {
		
		// figure out if we should engage units in friendly territory
		ArrayList<RobotInfo> targettableEnemies = new ArrayList<RobotInfo>();
		RobotInfo[] enemies = this.unitController.nearbyEnemies(300);
		for (RobotInfo enemy : enemies) {
			
			this.broadcaster.evaluateSeenLaunchersWithType(enemy.type);
			if (enemy.type == Missile.type()) continue;
			
			if (locationController.isLocationInFriendlyTerritory(enemy.location)) {
				
				targettableEnemies.add(enemy);
				
			}
			
		}
		return targettableEnemies.toArray(new RobotInfo[targettableEnemies.size()]);
		
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
