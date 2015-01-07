package team170;

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

	public RobotType getType() {
		return type();
	}
		
	public static RobotType type() {
		return RobotType.TOWER;
	}
	
	public static int identifierInteger() {
		return 5;
	}

}
