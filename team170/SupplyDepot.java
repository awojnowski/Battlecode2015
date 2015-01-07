package team170;

import battlecode.common.*;

public class SupplyDepot extends Robot {

	public SupplyDepot(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();

		
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers

	public RobotType getType() {
		return type();
	}
		
	public static RobotType type() {
		return RobotType.SUPPLYDEPOT;
	}
	
	public static int identifierInteger() {
		return 6;
	}

}
