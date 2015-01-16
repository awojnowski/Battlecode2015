package team170;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class TrainingField extends BuildableRobot {

	public TrainingField(RobotController robotController) {
		
		super(robotController);
		
		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
	
				this.trySpawn(Commander.type());
				
			}
										
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TRAININGFIELD;
	}

}
