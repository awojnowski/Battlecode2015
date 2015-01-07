package team170;

import battlecode.common.*;

public class Soldier extends BattleRobot {

	public Soldier(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			Boolean attacked = attack();
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked || this.attackStyle == BattleRobotAttackStyle.STRAFE_ON_ATTACK;
				if (shouldMove) {
					
					if (!this.shouldMobilize()) {
						
						this.moveTo(this.randomDirection());
						
					}  else {
						
						this.mobilize();
						
					}
					
				}
				
			}
			this.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.SOLDIER;
	}
	
	public static int identifierInteger() {
		return 4;
	}

}
