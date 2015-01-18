package boyer01181424;

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

			this.robotController.setIndicatorString(1, "Budget: Commander = " + this.broadcaster.budgetForType(Commander.type()));
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
