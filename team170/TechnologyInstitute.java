package team170;

import battlecode.common.*;

public class TechnologyInstitute extends BuildableRobot {

	public TechnologyInstitute(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (Clock.getRoundNum() < 500) {
				
				RobotInfo[] nearbyEnemies = this.unitController.nearbyEnemies(200);
				if (nearbyEnemies.length > 1) {
					
					this.broadcaster.setEnemyTeamRushing(true);
					
				}
				
			}
			
			this.trySpawn(Computer.type());
			
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
