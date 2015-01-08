package team170;

import battlecode.common.*;

public class TankFactory extends Robot {

	public TankFactory(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Tank.type()) < 100) {
					
					this.trySpawn(this.randomDirection(), Tank.type());
					
				}
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.TANKFACTORY;
	}

}
