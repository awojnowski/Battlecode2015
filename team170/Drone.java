package team170;

import battlecode.common.*;

public class Drone extends BattleRobot {

	public Drone(RobotController robotController) {
		
		super(robotController);
		
		
		
	}

	public void run() {
		
		super.run();
		
		try {

			
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
			
			
			
		}
		
		this.robotController.yield();
		
	}
	
	public Boolean canMoveInHQRange(int totalTowers) throws GameActionException {
		
		return false;
		
	}
	
	public Boolean canMoveInTowerRange() throws GameActionException {

		return false;
		
	}
	
	public Boolean canMoveInMilitaryRange() throws GameActionException {
		
		return false;
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
