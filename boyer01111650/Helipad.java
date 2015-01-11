package boyer01111650;

import battlecode.common.*;

public class Helipad extends BuildableRobot {

	public Helipad(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(this.movementController.randomDirection(), Drone.type());
				
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
