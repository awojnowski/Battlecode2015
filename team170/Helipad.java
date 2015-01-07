package team170;

import battlecode.common.*;

public class Helipad extends Robot {

	public Helipad(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Drone.type()) < 50) {
					
					this.trySpawn(this.randomDirection(), Drone.type());
					
				}
				
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
