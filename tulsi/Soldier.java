package tulsi;

import battlecode.common.*;

public class Soldier extends BattleRobot {

	public Soldier(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			attack();
			this.moveTo(this.randomDirection());
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 60;
	}
	
	public static RobotType type() {
		return RobotType.SOLDIER;
	}

}
