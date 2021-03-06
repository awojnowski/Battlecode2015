package boyer01171630;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class TechnologyInstitute extends BuildableRobot {

	public TechnologyInstitute(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TECHNOLOGYINSTITUTE;
	}

}
