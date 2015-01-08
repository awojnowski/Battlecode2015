package team170;

import battlecode.common.*;

public class Launcher extends Robot {

	public Launcher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
				
				
				
			}
			this.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.LAUNCHER;
	}

}