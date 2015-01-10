package 11540110boyerv1.locations;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class LocationController {
	
	public RobotController robotController;
	public Random random;
	
	// MARK: Static Variables
	
	public final Direction[] DIRECTIONS = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	
	// MARK: Init
	
	public LocationController() {
		
		
		
	}
	
	// MARK: Directions
	
	public Direction randomDirection() {
		
		int rand = this.random.nextInt(DIRECTIONS.length);
		return DIRECTIONS[rand];
		
	}
	
	// MARK: Distance
	
	public int distanceTo(MapLocation location) {
		
		return this.robotController.getLocation().distanceSquaredTo(location);
		
	}

	// MARK: Locations
	
	public MapLocation currentLocation() {
		
		return this.robotController.getLocation();
		
	}
	
	public MapLocation[] towerLocations() {
		
		return this.robotController.senseTowerLocations();
		
	}
	
	public MapLocation militaryRallyLocation() {
		
		MapLocation bestLocation = null;
		int closestTowerDistance = Integer.MAX_VALUE;
		
		MapLocation[] towers = this.towerLocations();
		for (MapLocation tower : towers) {
			
			int distance = this.enemyHQLocation().distanceSquaredTo(tower);
			if (distance < closestTowerDistance) {
				
				bestLocation = tower;
				closestTowerDistance = distance;
				
			}
			
		}
		
		if (bestLocation == null) {
			
			bestLocation = this.HQLocation();
			
		}
		
		return bestLocation;
		
	}
	
	public MapLocation HQLocation() {
		
		return this.robotController.senseHQLocation();
		
	}
	
	// MARK: Locations (Enemy)

	public MapLocation[] enemyTowerLocations() {
		
		return this.robotController.senseEnemyTowerLocations();
		
	}
	
	public MapLocation bestObjective() {
		
		MapLocation bestLocation = null;
		int closestTowerDistance = Integer.MAX_VALUE;
		
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = this.enemyHQLocation().distanceSquaredTo(tower);
			if (distance < closestTowerDistance) {
				
				bestLocation = tower;
				closestTowerDistance = distance;
				
			}
			
		}
		
		if (bestLocation == null) {
			
			bestLocation = this.enemyHQLocation();
			
		}
		
		return bestLocation;
		
	}
	
	public MapLocation enemyHQLocation() {
		
		return this.robotController.senseEnemyHQLocation();
		
	}

}
