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
			
		} else if (robotController.getType() == MinerFactory.type()) {
			
			robot = new MinerFactory(robotController);
			
		} else if (robotController.getType() == Miner.type()) {
			
			robot = new Miner(robotController);
			
		} else if (robotController.getType() == TankFactory.type()) {
			
			robot = new TankFactory(robotController);
			
		} else if (robotController.getType() == Tank.type()) {
			
			robot = new Tank(robotController);
			
		}
		
		// run the robot
		
		while(true) {
			
            if (robot != null) {
            	
            	robot.run();
            	
            }
			
		}
	
	}

}
