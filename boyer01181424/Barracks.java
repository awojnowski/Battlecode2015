package boyer01181424;

import battlecode.common.*;

public class Barracks extends BuildableRobot {

	public Barracks(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
			
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Soldier = " + this.broadcaster.budgetForType(Soldier.type()) + " Basher = " + this.broadcaster.budgetForType(Basher.type()));
			if (this.broadcaster.hasSeenLaunchers()) this.robotController.setIndicatorString(2, "DEFCON 1 LAUNCHES HAVE BEEN SPOTTED");
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
