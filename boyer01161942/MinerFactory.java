package boyer01161942;

import battlecode.common.*;

public class MinerFactory extends BuildableRobot {

	public MinerFactory(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Miner = " + this.broadcaster.budgetForType(Miner.type()));
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Miner.type());
				
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
