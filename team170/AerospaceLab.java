package team170;

import battlecode.common.*;

public class AerospaceLab extends Robot {

	public AerospaceLab(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Launcher.type()) < 50) {
					
					this.trySpawn(this.randomDirection(), Launcher.type());
					
				}
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.AEROSPACELAB;
	}

}
