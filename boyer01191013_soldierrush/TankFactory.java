package boyer01191013_soldierrush;

import battlecode.common.*;

public class TankFactory extends BuildableRobot {

	public TankFactory(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.robotController.setIndicatorString(1, "Budget: Tank = " + this.broadcaster.budgetForType(Tank.type()));
			
			if (this.robotController.isCoreReady()) {

				this.trySpawn(Tank.type());
				
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.TANKFACTORY;
	}

}
