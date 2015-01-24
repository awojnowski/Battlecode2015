package team170.locations;

import battlecode.common.*;
import team170.*;

public class LocationController {
	
	public RobotController robotController;
	
	// MARK: Distance
	
	public int distanceTo(MapLocation location) {
		
		return this.robotController.getLocation().distanceSquaredTo(location);
		
	}
	
	// MARK: Location Helpers
	
	public static RobotInfo closestUnitTo(MapLocation location, RobotInfo[] units) {
		
		RobotInfo closest = null;
		int closestDistance = Integer.MAX_VALUE;
		
		for (RobotInfo unit : units) {
			
			int distance = location.distanceSquaredTo(unit.location);
			if (distance < closestDistance) {
				
				closest = unit;
				closestDistance = distance;
				
			}
			
		}
		return closest;
		
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

		MapLocation HQLocation = this.HQLocation();
		MapLocation enemyHQLocation = this.enemyHQLocation();
		MapLocation middleLocation = new MapLocation((enemyHQLocation.x + HQLocation.x) / 2, (enemyHQLocation.y + HQLocation.y) / 2);
		
		MapLocation[] towers = this.towerLocations();
		for (MapLocation tower : towers) {
			
			int distance = middleLocation.distanceSquaredTo(tower);
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
	
	public MapLocation innerMostEnemyTowerLocation() {
		
		MapLocation closestTower = null;
		int closestTowerDistance = Integer.MAX_VALUE;
		
		MapLocation location = this.currentLocation();
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = location.distanceSquaredTo(tower);
			if (distance < closestTowerDistance) {
				
				closestTower = tower;
				closestTowerDistance = distance;
				
			}
			
		}
		return closestTower;
		
	}
	
	public MapLocation bestObjective() {
		
		MapLocation bestLocation = null;
		int closestTowerDistance = Integer.MAX_VALUE;
		
		MapLocation location = this.currentLocation();
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = location.distanceSquaredTo(tower);
			if (distance < closestTowerDistance) {
				
				bestLocation = tower;
				closestTowerDistance = distance;
				
			}
			
		}
		
		// if there are no towers or if the closest tower is further than the HQ, go to the HQ
		if (bestLocation == null || closestTowerDistance >= this.distanceTo(this.enemyHQLocation())) {
			
			bestLocation = this.enemyHQLocation();
			
		}
		
		return bestLocation;
		
	}
	
	public MapLocation enemyHQLocation() {
		
		return this.robotController.senseEnemyHQLocation();
		
	}
	
	// MARK: Towers
	
	public MapLocation enemyTowerInRange() {
		
		MapLocation location = this.currentLocation();
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = location.distanceSquaredTo(tower);
			if (distance < Tower.type().attackRadiusSquared) {
				
				return tower;
				
			}
			
		}
		return null;
		
	}

}
