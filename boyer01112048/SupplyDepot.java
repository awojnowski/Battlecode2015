package boyer01112048;

import battlecode.common.*;

public class SupplyDepot extends BuildableRobot {

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
