package team170;

import battlecode.common.*;

public class Tank extends BattleRobot {
	
	public Tank(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.robotController.setIndicatorString(2, "");
			
			AttackResult attackResult = new AttackResult();
			Boolean attacked = this.attack(attackResult);
			
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked;
				if (shouldMove) {
					
					RobotInfo[] dangerousEnemies = this.nearbyDangerousEnemies();
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
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TANK;
	}

}
