package team170;

import battlecode.common.*;
import team170.*;

public class CivicRobot extends Robot {

	public CivicRobot(RobotController robotController) {
		
		super(robotController);
		
		try {
			this.broadcaster.decrementCivicBuildingRobotCountFor(this.type);
		}
		catch (GameActionException exception){} 
		
	}

}
