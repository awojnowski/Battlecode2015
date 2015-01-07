package team170;

import battlecode.common.*;

public class Barracks extends Robot {

	public Barracks(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {
				
				if (((this.broadcaster.robotCountFor(Soldier.type()) < 200) && Clock.getRoundNum() < 1000) ||
					((this.broadcaster.robotCountFor(Soldier.type()) < 20) && Clock.getRoundNum() >= 1000)) {
					
					this.trySpawn(this.randomDirection(), Soldier.type());
					
				}
				
			}
										
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.BARRACKS;
	}

}
