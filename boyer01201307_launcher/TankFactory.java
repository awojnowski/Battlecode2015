package boyer01201307_launcher;

import battlecode.common.*;

public class TankFactory extends BuildableRobot {

	public TankFactory(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
						
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
