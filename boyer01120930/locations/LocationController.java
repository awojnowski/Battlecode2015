package boyer01120930.locations;

import battlecode.common.*;

public class LocationController {
	
	public RobotController robotController;
	
	// MARK: Distance
	
	public int distanceTo(MapLocation location) {
		
		return this.robotController.getLocation().distanceSquaredTo(location);
		
	}

	// MARK: Locations
	
	public MapLocation currentLocation() {
		
		return this.robotController.getLocation();
		
	}
	
	public Boolean isLocationInFriendlyTerritory(MapLocation location) {
		
		return (location.distanceSquaredTo(this.enemyHQLocation()) * 1.2 > this.distanceTo(this.HQLocation()));
		
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
