package team170;

import battlecode.common.*;

public class Drone extends BattleRobot {
	
	private int moveRefreshCount;
	private MapLocation targetLocation;

	public Drone(RobotController robotController) {
		
		super(robotController);
		
		this.moveRefreshCount = 0;
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.attack();
			if (this.robotController.isCoreReady()) {
				
				if (this.targetLocation == null) this.refreshTargetLocation();
				if (this.movementController.moveToward(this.targetLocation) != null) {
					
					this.moveRefreshCount ++;
					
				}
				
				if (this.moveRefreshCount > 30) {
					
					this.moveRefreshCount = 0;
					this.refreshTargetLocation();
					
				}
								
			}
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	private void refreshTargetLocation(){ 

		MapLocation hqLocation = this.locationController.enemyHQLocation();
		MapLocation[] towerLocations = this.locationController.enemyTowerLocations();
		
		Boolean isHQ = false;
		MapLocation closestLocation = null;
		double closestDistance = Integer.MAX_VALUE;
		
		for (int i = 0; i < towerLocations.length + 1; i++) {
			
			MapLocation location = null;
			if (i == towerLocations.length) location = hqLocation;
			else location = towerLocations[i];
			
			double distance = this.locationController.currentLocation().distanceSquaredTo(location);
			if (distance < closestDistance) {
				
				closestLocation = location;
				closestDistance = distance;
				isHQ = (location == hqLocation);
				
			}
			
		}
		
		Direction direction = this.locationController.currentLocation().directionTo(closestLocation);
		double radius = Tower.type().attackRadiusSquared;
		if (isHQ) radius = HQ.type().attackRadiusSquared;
		
		this.targetLocation = closestLocation.add(direction, (int)Math.sqrt(radius));
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
