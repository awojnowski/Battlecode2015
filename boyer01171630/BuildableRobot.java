package boyer01171630;

import battlecode.common.*;

public class BuildableRobot extends Robot {

	public BuildableRobot(RobotController robotController) {
		
		super(robotController);
		
		try {
			this.broadcaster.finishBuildingRobot(this.type);
		}
		catch (GameActionException exception){} 
		
	}

}
