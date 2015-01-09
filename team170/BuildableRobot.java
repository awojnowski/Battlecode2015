package team170;

import battlecode.common.*;

public class BuildableRobot extends Robot {

	public BuildableRobot(RobotController robotController) {
		
		super(robotController);
		
		try {
			this.broadcaster.decrementBuildingRobotCountFor(this.type);
		}
		catch (GameActionException exception){} 
		
	}

}
