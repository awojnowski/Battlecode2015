package tulsi;

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
	
	public static int cost() {
		return 100;
	}
		
	public static RobotType type() {
		return RobotType.SUPPLYDEPOT;
	}


}
