package team170;

import battlecode.common.*;

public class Tank extends BattleRobot {

	public Tank(RobotController robotController) {
		
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
						
						MapLocation rallyLocation = this.locationController.militaryRallyLocation();
						if (this.locationController.distanceTo(rallyLocation) > 18) {

							this.movementController.moveToward(rallyLocation);
							
						} else {
							
							this.movementController.moveTo(this.locationController.randomDirection());
							
						}
						
					} else {

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
		return RobotType.TANK;
	}

}
