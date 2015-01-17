package boyer01171630;

import battlecode.common.*;

public class Barracks extends BuildableRobot {

	public Barracks(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Soldier = " + this.broadcaster.budgetForType(Soldier.type()));
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Soldier.type());
				
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
