package boyer01161942;

import battlecode.common.*;

public class Helipad extends BuildableRobot {

	public Helipad(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Drone = " + this.broadcaster.budgetForType(Drone.type()));
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Drone.type());
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.HELIPAD;
	}

}
