package boyer01161942.units;

import battlecode.common.*;
import boyer01161942.*;

public class UnitController {

	public Robot robot;
	
	public UnitController() {
		
		
		
	}
	
	// MARK: Attackers
	
	public int militaryAttackersWithinRange() throws GameActionException {
		
		int count = 0;
		MapLocation location = this.robot.locationController.currentLocation();
		
		RobotInfo[] enemies = this.nearbyEnemies();
		for (RobotInfo enemy : enemies) {
			
			if (!isUnitTypeMilitary(enemy.type)) continue;
			if (location.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared) count ++;
			
		}
		return count;
		
	}
	
	public RobotInfo closestMilitaryAttackerWithinRange() throws GameActionException {
		
		MapLocation location = this.robot.locationController.currentLocation();
		double closestDistance = Integer.MAX_VALUE;
		RobotInfo closestRobot = null;
		
		RobotInfo[] enemies = this.nearbyEnemies();
		for (RobotInfo enemy : enemies) {
			
			if (!isUnitTypeMilitary(enemy.type)) continue;
			
			double distance = location.distanceSquaredTo(enemy.location); 
			if (distance <= enemy.type.attackRadiusSquared && distance < closestDistance) {
				
				closestDistance = distance;
				closestRobot = enemy;
				
			}
			
		}
		return closestRobot;
		
	}
	
	// MARK: Nearby Units

	public MapLocation[] enemyTowers() throws GameActionException {
		
		return this.robot.robotController.senseEnemyTowerLocations();
		
	}

	public RobotInfo[] nearbyEnemies() throws GameActionException {
		
		return this.nearbyEnemies(HQ.type().sensorRadiusSquared);
		
	}

	public RobotInfo[] nearbyEnemies(int sensorRadius) throws GameActionException {

		return this.robot.robotController.senseNearbyRobots(sensorRadius, this.robot.team.opponent());
		
	}
	
	public RobotInfo closestNearbyEnemy() throws GameActionException {
		
		return this.closestNearbyEnemy(HQ.type().sensorRadiusSquared);
		
	}
	
	public RobotInfo closestNearbyEnemy(int sensorRadius) throws GameActionException {
		
		RobotInfo closest = null;
		double distance = Double.MAX_VALUE;
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		RobotInfo[] enemies = this.nearbyEnemies(sensorRadius);
		for (RobotInfo enemy : enemies) {
			
			double enemyDistance = currentLocation.distanceSquaredTo(enemy.location);
			if (enemyDistance < distance) {
				
				closest = enemy;
				distance = enemyDistance;
				
			}
			
		}
		
		return closest;
		
	}
	
	public RobotInfo[] nearbyAttackableEnemies() throws GameActionException {
		
		int attackRadius = this.robot.type.attackRadiusSquared;
		if (this.robot.type == HQ.type()) attackRadius = HQ.friendlyAttackRadiusSquared(this.robot.locationController.enemyTowerLocations().length);
		return this.robot.robotController.senseNearbyRobots(attackRadius, this.robot.team.opponent());
		
	}
	
	public RobotInfo[] nearbyAllies() throws GameActionException {
		
		return this.robot.robotController.senseNearbyRobots(this.robot.type.sensorRadiusSquared, this.robot.team);
		
	}
	
	public RobotInfo[] allAllies() throws GameActionException {
		
		return this.robot.robotController.senseNearbyRobots((int)Math.pow(256, 2), this.robot.team);
		
	}
	
	// MARK: Unit Helpers (Classifications)
	
	public static Boolean isUnitTypeMiner(RobotType type) {
		
		return (type == Beaver.type() || type == Miner.type());
		
	}
	
	public static Boolean isUnitTypeMilitary(RobotType type) {
		
		return (type == Drone.type() || type == Launcher.type() || type == Missile.type() || type == Soldier.type() || type == Tank.type() || type == RobotType.BASHER || type == Commander.type());
		
	}
	
	public static Boolean isUnitTypePassiveBuilding(RobotType type){ 
		
		return (type == AerospaceLab.type() || type == Barracks.type() || type == Helipad.type() || type == MinerFactory.type() || type == SupplyDepot.type() || type == TankFactory.type() || type == RobotType.HANDWASHSTATION || type == TechnologyInstitute.type() || type == TrainingField.type());
		
	}
	
	public static Boolean isUnitTypeAggressiveBuilding(RobotType type) {

		return (type == HQ.type() || type == Tower.type());
		
	}
	
	// MARK: Unit Helpers (Generalizations)
	
	public static Boolean isUnitTypeBuilding(RobotType type) {
		
		return isUnitTypeAggressiveBuilding(type) || isUnitTypePassiveBuilding(type);
		
	}
	
	public static Boolean isUnitTypeDangerous(RobotType type) {
		
		return isUnitTypeAggressiveBuilding(type) || isUnitTypeMilitary(type);
		
	}

}
