package team170;

import battlecode.common.*;

public class MinerFactory extends CivicRobot {

	public MinerFactory(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(this.randomDirection(), Miner.type());
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINERFACTORY;
	}

}