package boyer01111650;

import battlecode.common.*;

public class Launcher extends BattleRobot {

	public Launcher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
			try {
				
				if (this.robotController.getMissileCount() > 0) {

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
					
					if (!this.shouldMobilize()) {
						
						MapLocation rallyLocation = this.locationController.militaryRallyLocation();
						if (this.locationController.distanceTo(rallyLocation) > 18) {
	
							this.movementController.moveToward(rallyLocation);
							
						} else {
							
							this.movementController.moveTo(this.movementController.randomDirection());
							
						}
						
					} else {
	
						this.mobilize();
						
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
