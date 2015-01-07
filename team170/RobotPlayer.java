package team170;

import battlecode.common.*;

public class RobotPlayer {
	
	public static void run(RobotController robotController) {
		
		Robot robot = null;
		
		// parse the current robot
		
		if (robotController.getType() == HQ.type()) {

			robot = new HQ(robotController);
			
		} else if (robotController.getType() == SupplyDepot.type()) {
			
			robot = new SupplyDepot(robotController);
			
		} else if (robotController.getType() == Beaver.type()) {

			robot = new Beaver(robotController);
			
		} else if (robotController.getType() == Soldier.type()) {
			
			robot = new Soldier(robotController);
			
		} else if (robotController.getType() == Barracks.type()) {
			
			robot = new Barracks(robotController);
			
		} else if (robotController.getType() == Tower.type()) {
			
			robot = new Tower(robotController);
			
		}
		
		// run the robot
		
		while(true) {
			
            if (robot != null) {
            	
            	robot.run();
            	
            }
			
		}
	
	}

}
