package boyer01161509;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Commander extends BattleRobot {
	
	public Commander(RobotController robotController) {
		
		super(robotController);
				
	}

	public void run() {
		
		super.run();
		
		try {
									
			AttackResult attackResult = new AttackResult();
			Boolean attacked = this.attack(attackResult);
			
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked;
				if (shouldMove) {
					
					if (!this.shouldMobilize()) {

						this.move();
						
					} else {

						this.mobilize();
						
					}
					
				}
				
			}
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Movement
	
	private void move() throws GameActionException {
		
		MapLocation moveLocation = this.locationController.militaryRallyLocation();
		if (this.locationController.distanceTo(moveLocation) > 18) {

			this.movementController.moveToward(moveLocation);
			
		} else {
			
			this.movementController.moveTo(this.movementController.randomDirection());
			
		}
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.COMMANDER;
	}

}
