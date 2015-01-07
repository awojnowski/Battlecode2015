package tulsi;

import battlecode.common.*;

public class Barracks extends Robot {

	public Barracks(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
				
		try {
							
			this.trySpawn(this.randomDirection(), Soldier.type());
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 300;	
	}
	
	public static RobotType type() {
		return RobotType.BARRACKS;
	}

}
