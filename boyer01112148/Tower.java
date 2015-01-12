package boyer01112148;

import battlecode.common.*;

public class Tower extends BattleRobot {

	public Tower(RobotController robotController) {
		
		super(robotController);

		this.canBeMobilized = false;
		
	}

	public void run() {
		
		super.run();

		try {
			
			this.attack();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.TOWER;
	}

}
