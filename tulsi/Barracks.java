package tulsi;

import battlecode.common.*;

public class Barracks extends Robot {

	public Barracks(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
				
		try {
							
			if (this.robotController.hasSpawnRequirements(Soldier.type())) {
				
				Direction direction = this.randomDirection();
				if (this.robotController.canSpawn(direction, Soldier.type())) {
					
					this.robotController.spawn(direction, Soldier.type());
					
				}
					
			}
							
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
