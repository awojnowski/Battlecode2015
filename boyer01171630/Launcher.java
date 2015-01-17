package boyer01171630;

import battlecode.common.*;

public class Launcher extends BattleRobot {

	public Launcher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
			try {
				
				if (this.robotController.getMissileCount() > 2) {

					RobotInfo[] nearbyEnemies = this.unitController.nearbyEnemies();
					RobotInfo desiredEnemy = this.desiredEnemy(nearbyEnemies);
					if (desiredEnemy != null) {
						
						Direction direction = this.locationController.currentLocation().directionTo(desiredEnemy.location);
						if (this.robotController.canLaunch(direction)) {
							
							this.broadcaster.setNextMissileDirection(direction);
							this.robotController.launchMissile(direction);
							
						}
						
					}
					
				}
				
							
				if (this.robotController.isCoreReady()) {
					
					Boolean shouldMove = true;
					if (shouldMove) {

						RobotInfo[] dangerousEnemies = this.nearbyDangerousEnemies(25, 25);
						if (dangerousEnemies != null && dangerousEnemies.length > 0) {
							
							this.movementController.moveAway(dangerousEnemies);
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
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.LAUNCHER;
	}

}
