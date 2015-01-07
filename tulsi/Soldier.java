package tulsi;

import battlecode.common.*;

public class Soldier extends BattleRobot {

	public Soldier(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			Boolean attacked = attack();
			Boolean shouldMove = !attacked || this.attackStyle == BattleRobotAttackStyle.STRAFE_ON_ATTACK;
			if (shouldMove) {
				
				if (this.robotController.isCoreReady()) {
					
					if (!this.shouldMobilize()) {
						
						this.moveTo(this.randomDirection());
						
					}  else {
						
						this.mobilize();
						
					}
					
				}
				
			}
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.SOLDIER;
	}

}
