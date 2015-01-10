package team170;

import battlecode.common.*;

public class Drone extends BattleRobot {

	public Drone(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.attack();
			if (this.robotController.isCoreReady()) {
				
				MapLocation location = this.locationController.enemyHQLocation();
				this.movementController.moveToward(location);
								
			}
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
