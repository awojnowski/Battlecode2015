package boyer01161942;

import battlecode.common.*;

public class AerospaceLab extends BuildableRobot {

	public AerospaceLab(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Launchers = " + this.broadcaster.budgetForType(Launcher.type()));
			
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
