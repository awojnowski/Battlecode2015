package team170;

import battlecode.common.*;

public class Launcher extends BattleRobot {

	public Launcher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
			try {
				
				this.robotController.setIndicatorString(1, "Nearby enemies: " + this.unitController.nearbyEnemies());
				
				if (this.robotController.isCoreReady()) {
					
					if (!this.shouldMobilize()) {
						
						MapLocation rallyLocation = this.locationController.militaryRallyLocation();
						if (this.locationController.distanceTo(rallyLocation) > 18) {
	
							this.movementController.moveToward(rallyLocation);
							
						} else {
							
							this.movementController.moveTo(this.locationController.randomDirection());
							
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
