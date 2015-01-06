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
			
			if (Clock.getRoundNum() < 500) {
				
				this.moveTo(this.randomDirection());
				
			} else {
				
				this.moveToward(this.enemyHQLocation());
				
			}
			
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
