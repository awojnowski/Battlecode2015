package team170;

import battlecode.common.*;

public class TechnologyInstitute extends BuildableRobot {

	public TechnologyInstitute(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {

			RobotInfo[] nearbyEnemies = this.unitController.nearbyMilitaryEnemies(200);
			if (nearbyEnemies.length > 1 && Clock.getRoundNum() < 500) {
				
				this.broadcaster.setEnemyTeamRushing(true);
				
			}
			
		}
		catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TECHNOLOGYINSTITUTE;
	}

}
