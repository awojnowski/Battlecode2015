package boyer01191013_soldierrush;

import java.util.ArrayList;

import battlecode.common.*;
import boyer01191013_soldierrush.movement.*;
import boyer01191013_soldierrush.units.*;

public class Drone extends BattleRobot {
	
	private int moveRefreshCount;
	private MapLocation targetLocation;
	private MapLocation lastPatrolLocation; // stores where they were last sitting to patrol HQ
	private int patrolDirection; 			// switches if they are in the same place
	
	private Boolean allowFurtherTargetTravel = true;

	public Drone(RobotController robotController) {
		
		super(robotController);
		
		this.moveRefreshCount = 0;
		this.lastPatrolLocation = null;
		this.patrolDirection = 1;
		
		try {

			this.setTargetLocation( this.leftOrRightOfHQ(20), true);
			
		}
		catch (GameActionException e) {
			
			this.setTargetLocation(this.locationController.enemyHQLocation(), true);
			
		}
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.robotController.setIndicatorLine(this.locationController.currentLocation(), this.targetLocation, 255, 0, 0);
			
			Boolean attacked = this.attack();
			if (this.robotController.isCoreReady()) {
				
				RobotInfo[] dangerousEnemies = this.nearbyDangerousEnemies();
				if (dangerousEnemies != null && dangerousEnemies.length > 0) {
					
					this.movementController.moveAway(dangerousEnemies);
					
				} else {
					
					if (!attacked) {
						
						this.movementController.moveToward(this.targetLocation, this.allowFurtherTargetTravel);
						this.moveRefreshCount ++;
						
						if (this.moveRefreshCount > 10) { // The refresh is fairly low, but helps them patrol faster
							
							this.moveRefreshCount = 0;
							if (!this.currentPlaystyle().areDronesRestrictedToMiners()) {

								this.refreshTargetLocation();
								
							} else {
								
								this.refreshScoutTargetLocation();
								
							}
							
						}
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	private Boolean refreshNearbyBuildingTargets() throws GameActionException {
		
		final MapLocation[] towers = this.locationController.enemyTowerLocations();
		final MapLocation enemyHQLocation = this.robotController.senseEnemyHQLocation();
		
		final int HQAttackRadius = HQ.enemyAttackRadiusSquared(towers.length);
		final int untargettableHQRadius = HQAttackRadius - this.type.attackRadiusSquared;
		final int untargettableTowerRadius = Tower.type().attackRadiusSquared - this.type.attackRadiusSquared;
		
		// initially try to target any buildings that we see
		
		ArrayList<RobotInfo> targettableEnemies = new ArrayList<RobotInfo>();
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
					
					targettableEnemies.add(enemy);
					
				}
				
			}
			
		}
		
		if (targettableEnemies.size() > 0) {
			
			RobotInfo enemy = this.desiredEnemy(targettableEnemies.toArray(new RobotInfo[targettableEnemies.size()]));
			if (enemy != null) { 

				this.setTargetLocation(enemy.location, false);
				return true;
				
			}
			
		}
		return false;
		
	}
	
	private void refreshTargetLocation() throws GameActionException {
		
		// Attack passive buildings by default
		if (this.refreshNearbyBuildingTargets()) return;
		
		final MapLocation[] towers = this.locationController.enemyTowerLocations();
		final MapLocation enemyHQLocation = this.robotController.senseEnemyHQLocation();
		
		final int HQAttackRadius = HQ.enemyAttackRadiusSquared(towers.length);

		// no buildings, so let's try find somewhere to go
		final MapLocation robotLocation = this.robotController.getLocation();
		
		if (this.robotController.getLocation().distanceSquaredTo(enemyHQLocation) < HQAttackRadius * 2.0 ) {
			
			// Patrol around HQ if it's already there
			
			final int patrolAmount = 2; // fast and unreliable at 1, slow and reliable at 3, 2 is the sweet spot
			final int estimatedHQRange = 6;
			
			int directionToHqInt = MovementController.directionToInt(robotLocation.directionTo(enemyHQLocation));
			Direction tangentDirection = MovementController.directionFromInt(directionToHqInt + patrolAmount * this.patrolDirection());
			
			this.setTargetLocation(enemyHQLocation.add(tangentDirection, estimatedHQRange), false);
			
		} else {  
			
			// Otherwise check if it's stuck, then strafe away from the towers if it is
			
			if (this.lastPatrolLocation != null) {
				
				if (robotLocation.distanceSquaredTo(this.lastPatrolLocation) <= 1) { // stuck
					
					int directionToHqInt = MovementController.directionToInt(robotLocation.directionTo(enemyHQLocation));
					int leftStrafeDirection = MovementController.directionIndexFromInt(directionToHqInt - 2);
					int rightStrafeDirection = MovementController.directionIndexFromInt(directionToHqInt + 2);
					int towersOnLeft = 0;
					int towersOnRight = 0;
					
					for (MapLocation towerLocation : towers) {
						
						int directionToTowerInt = MovementController.directionToInt(robotLocation.directionTo(towerLocation)); 
						if (directionToTowerInt == leftStrafeDirection || MovementController.directionIndexFromInt(directionToHqInt + 1) == leftStrafeDirection || MovementController.directionIndexFromInt(directionToHqInt - 1) == leftStrafeDirection)
							towersOnLeft++;
						else if (directionToTowerInt == rightStrafeDirection || MovementController.directionIndexFromInt(directionToHqInt + 1) == rightStrafeDirection || MovementController.directionIndexFromInt(directionToHqInt - 1) == rightStrafeDirection)
							towersOnRight++;
						
					}
					
					int strafeDirection = 0;
					if (towersOnRight > towersOnLeft)
						strafeDirection = leftStrafeDirection;
					else
						strafeDirection = rightStrafeDirection;
					this.setTargetLocation(robotLocation.add(MovementController.directionFromInt(strafeDirection), 12), true);
					
				} else {

					this.setTargetLocation(enemyHQLocation, true);
					
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
	
	private MapLocation leftOrRightOfHQ(int distance) throws GameActionException {
		
		MapLocation robotLocation = this.robotController.getLocation();
		MapLocation enemyHQLocation = this.robotController.senseEnemyHQLocation();
		
		int directionToHQInt = MovementController.directionToInt(robotLocation.directionTo(enemyHQLocation));
		
		final int offset = (this.broadcaster.robotCountFor(this.type) % 2 == 0) ? 2 : -2;
		Direction tangentDirection = MovementController.directionFromInt(directionToHQInt + offset);
		
		return enemyHQLocation.add(tangentDirection, distance);
		
	}
	
	private void refreshScoutTargetLocation() throws GameActionException { 
		
		// Attack passive buildings by default
		if (this.refreshNearbyBuildingTargets()) return;
		
		this.setTargetLocation(this.locationController.enemyHQLocation().add(this.movementController.randomDirection(), 10 + this.random.nextInt(30)), true);
		
	}
	
	// MARK: Target Location Setter
	
	public void setTargetLocation(MapLocation location, Boolean allowFurtherTargetTravel) {
		
		this.allowFurtherTargetTravel = allowFurtherTargetTravel;
		this.targetLocation = location;
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
