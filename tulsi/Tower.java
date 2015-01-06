package tulsi;

import battlecode.common.*;

public class Tower extends BattleRobot {

	public Tower(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();

		try {
			
			attack();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 0;
	}
		
	public static RobotType type() {
		return RobotType.TOWER;
	}

}
