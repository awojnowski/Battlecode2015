package tulsi;

import battlecode.common.*;

public class RobotPlayer {
	
	public static void run(RobotController robotController) {
		
		Robot robot = null;
		
		// parse the current robot
		
		if (robotController.getType() == RobotType.HQ) {

			robot = new HQ(robotController);
			
		} else if (robotController.getType() == RobotType.BEAVER) {

			robot = new Beaver(robotController);
			
		}
		
		// run the robot
		
		while(true) {
			
            if (robot != null) {
            	
            	robot.run();
            	
            }
			
		}
	
	}

}
