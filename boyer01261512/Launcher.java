package boyer01261512;

import battlecode.common.*;

public class Launcher extends BattleRobot {

	public Launcher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
			try {
				
				final int missileCount = this.robotController.getMissileCount();
				if (missileCount > 0) {
					
					RobotInfo[] nearbyEnemies = this.unitController.nearbyEnemies();
					RobotInfo desiredEnemy = this.desiredEnemy(nearbyEnemies);
					if (desiredEnemy != null) {
						
						this.fireMissileAtLocation(desiredEnemy.location);
						
					} else {

						MapLocation objective = this.locationController.bestObjective();
						if (this.locationController.currentLocation().distanceSquaredTo(objective) < 50) {
							
							this.fireMissileAtLocation(objective);
							
						}
						
					}
					
				}
							
				if (this.robotController.isCoreReady()) {
					
					Boolean shouldMove = true;
					if (shouldMove) {

						RobotInfo enemy = this.unitController.closestNearbyEnemy(missileCount == 0 ? 70 : 35);
						if (enemy != null) {
							
							this.movementController.moveAway(enemy.location);
							shouldMove = false;								
							
						}	
						
					}
					
					if (shouldMove) {
						
						if (!this.shouldMobilize()) {
							
							RobotInfo[] enemiesInTerritory = this.enemiesInTerritory();
							if (enemiesInTerritory.length > 0) {
								
								RobotInfo enemy = this.desiredEnemy(enemiesInTerritory);
								MapLocation bestLocation = enemy.location;
								shouldMove = this.movementController.moveToward(bestLocation) == null;
								
							}
							
							// if we haven't moved toward an enemy location then we can go and stick to the plan
							if (shouldMove) {

								MapLocation rallyLocation = this.locationController.militaryRallyLocation();
								if (this.locationController.distanceTo(rallyLocation) > 18) {

									this.movementController.moveToward(rallyLocation);
									
								} else {
									
									this.movementController.moveTo(this.movementController.randomDirection());
									
								}
								
							}
							
						} else {

							this.mobilize();
							
						}
						
					}
					
				}
				this.supplyController.transferSupplyIfPossible();
				
			}
			catch (GameActionException exception) {}
		
		this.robotController.yield();
		
	}
	
	public Boolean canMoveInTowerRange() throws GameActionException {

		return false;
		
	}
	
	public Boolean canMoveInHQRange(int totalTowers) throws GameActionException {
		
		return false;
		
	}
	
	public Boolean canMoveInMilitaryRange() throws GameActionException {
		
		return true;
		
	}
	
	// MARK: Missiles
	
	private void fireMissileAtLocation(MapLocation location) throws GameActionException {
		
		Direction direction = this.locationController.currentLocation().directionTo(location);
		if (this.robotController.canLaunch(direction)) {
			
			this.broadcaster.setNextMissileDirection(direction);
			this.robotController.launchMissile(direction);
			
		}
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.LAUNCHER;
	}

}
