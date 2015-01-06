package tulsi;

import battlecode.common.*;

public class HQ extends BattleRobot {

	public HQ(RobotController robotController) {
		
		super(robotController);

		this.canBeMobalized = false;
		
	}

	public void run() {
		
		super.run();

		try {
			
			attack();
			
			double oreCount = this.robotController.getTeamOre();
			if (robotController.isCoreReady() && oreCount > 100) {
			
				robotController.spawn(this.randomDirection(), RobotType.BEAVER);
			
			}
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 0;
	}
		
	public static RobotType type() {
		return RobotType.HQ;
	}

}
