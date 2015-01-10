package team170;

import team170.units.UnitController;
import battlecode.common.*;

public class Drone extends BattleRobot {
	
	private int moveRefreshCount;
	private MapLocation targetLocation;
	private MapLocation lastPatrolLocation; // stores where they were last sitting to patrol HQ
	private int patrolDirection; 			// switches if they are in the same place

	public Drone(RobotController robotController) {
		
		super(robotController);
		
		this.moveRefreshCount = 0;
		this.targetLocation = this.robotController.senseEnemyHQLocation();
		this.lastPatrolLocation = null;
		this.patrolDirection = 1;
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (!this.attack()) {
					
				if (this.robotController.isCoreReady()) {
					
					this.movementController.moveToward(this.targetLocation);
					this.moveRefreshCount ++;
					
					if (this.moveRefreshCount > 10) { // The refresh is fairly low, but helps them patrol faster
						
						this.moveRefreshCount = 0;
						this.refreshTargetLocation();
						
					}
									
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	private void refreshTargetLocation() throws GameActionException { 
		
		// Attack passive buildings by default
		
		final MapLocation[] towers = this.locationController.enemyTowerLocations();
		final MapLocation enemyHQLocation = this.robotController.senseEnemyHQLocation();
		
		final int HQAttackRadius = HQ.attackRadiusSquared(towers.length);
		final int untargettableHQRadius = HQAttackRadius - this.type.attackRadiusSquared;
		final int untargettableTowerRadius = Tower.type().attackRadiusSquared - this.type.attackRadiusSquared;
		
		// initially try to target any buildings that we see
		
		RobotInfo[] enemies = this.unitController.nearbyEnemies();
		for (RobotInfo enemy : enemies) {
			
			if (UnitController.isUnitTypePassiveBuilding(enemy.type)) {
				
				// check to see if the building that we can see is within where we can attack
				
				Boolean targettable = true;
				for (MapLocation towerLocation : towers) {
					
					if (enemy.location.distanceSquaredTo(towerLocation) <= untargettableTowerRadius) {
						
						targettable = false;
						break;
						
					}
					
				}
				
				if (enemy.location.distanceSquaredTo(enemyHQLocation) <= untargettableHQRadius) {
					
					targettable = false;
					break;
					
				}
				
				if (targettable) {
					
					// we can target this building so that's our new target location
					this.targetLocation = enemy.location;
					return;
					
				}
				
			}
			
		}

		// no buildings, so let's try find somewhere to go
		final MapLocation robotLocation = this.robotController.getLocation();
		
		if (this.robotController.getLocation().distanceSquaredTo(enemyHQLocation) < HQAttackRadius * 2.0 ) {
			
			// Patrol around HQ if it's already there
			
			final int patrolAmount = 2; // fast and unreliable at 1, slow and reliable at 3, 2 is the sweet spot
			final int estimatedHQRange = 6;
			
			int directionToHqInt = this.movementController.directionToInt(robotLocation.directionTo(enemyHQLocation));
			Direction tangentDirection = this.locationController.DIRECTIONS[(directionToHqInt + patrolAmount * this.patrolDirection() + 8) % 8];
			
			this.targetLocation = enemyHQLocation.add(tangentDirection, estimatedHQRange);
			
		} else {  
			
			// Otherwise check if it's stuck, then strafe away from the towers if it is
			
			if (this.lastPatrolLocation != null) {
				
				if (robotLocation.distanceSquaredTo(this.lastPatrolLocation) < 1) { // stuck
					
					int directionToHqInt = this.movementController.directionToInt(robotLocation.directionTo(enemyHQLocation));
					int leftStrafeDirection = (directionToHqInt - 2 + 8) % 8;
					int rightStrafeDirection = (directionToHqInt + 2) % 8;
					int towersOnLeft = 0;
					int towersOnRight = 0;
					
					for (MapLocation towerLocation : towers) {
						
						int directionToTowerInt = this.movementController.directionToInt(robotLocation.directionTo(towerLocation)); 
						
						if (directionToTowerInt == leftStrafeDirection || (directionToTowerInt+1)%8 == leftStrafeDirection || (directionToTowerInt+1)%8 == leftStrafeDirection)
							towersOnLeft++;
						else if (directionToTowerInt == rightStrafeDirection || (directionToTowerInt+1)%8 == rightStrafeDirection || (directionToTowerInt+1)%8 == rightStrafeDirection)
							towersOnRight++;
						
					}
					
					if (towersOnRight > towersOnLeft)
						this.targetLocation = robotLocation.add(this.locationController.DIRECTIONS[leftStrafeDirection], 12);
					else
						this.targetLocation = robotLocation.add(this.locationController.DIRECTIONS[rightStrafeDirection], 12);
					
				} else {
					
					this.targetLocation = enemyHQLocation;
					
				}
				
			}
			
			this.lastPatrolLocation = robotLocation;
			
		}
		
	}
	
	private int patrolDirection() {
		
		MapLocation currLocation = this.robotController.getLocation();
		
		if (this.lastPatrolLocation != null) { // if this isn't the first movement, switch direction if we haven't moved
			
			if (currLocation.distanceSquaredTo(this.lastPatrolLocation) < 1) {
				
				if (this.patrolDirection == 1)
					this.patrolDirection = -1;
				else
					this.patrolDirection = 	1;
				
			}
			
		}
		
		this.lastPatrolLocation = currLocation;
		
		return this.patrolDirection;
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
