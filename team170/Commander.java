package team170;

import battlecode.common.*;

public class Commander extends BattleRobot {
		
	private boolean isHarassing;
	
	public Commander(RobotController robotController) {
		
		super(robotController);
		
		
				
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.attack();

			Boolean canMove = true;
			if (canMove) {
				
				RobotInfo[] enemiesInRange = this.unitController.nearbyEnemiesWithinTheirAttackRange();
				if (enemiesInRange.length > 0) {
					
					canMove = (this.movementController.moveAway(enemiesInRange) != null);
					
				}
				
			}
			
			if (this.isHarassing) {
				
				if (canMove) {

					canMove = (this.movementController.moveToward(this.locationController.enemyHQLocation()) != null);
					
				}
				
			} else {
				
				if (!this.shouldMobilize()) {
					
					if (canMove) {

						RobotInfo[] enemiesInTerritory = this.enemiesInTerritory();
						if (enemiesInTerritory.length > 0) {
							
							RobotInfo enemy = this.desiredEnemy(enemiesInTerritory);
							MapLocation bestLocation = enemy.location;
							canMove = this.movementController.moveToward(bestLocation) == null;
							
						}
						
					}
					
					// if we haven't moved toward an enemy location then we can go and stick to the plan
					if (canMove) {

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
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.COMMANDER;
	}

}
