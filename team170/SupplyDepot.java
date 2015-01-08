package team170;

import battlecode.common.*;

public class SupplyDepot extends CivicRobot {

	public SupplyDepot(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();

		
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.SUPPLYDEPOT;
	}

}
