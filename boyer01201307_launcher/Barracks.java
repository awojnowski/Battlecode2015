package boyer01201307_launcher;

import battlecode.common.*;

public class Barracks extends BuildableRobot {

	public Barracks(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
		try {
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Soldier.type());
				this.trySpawn(Basher.type());
				
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
