package boyer01201307_launcher;

import battlecode.common.*;

public class AerospaceLab extends BuildableRobot {

	public AerospaceLab(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
						
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Launcher.type());
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.AEROSPACELAB;
	}

}
