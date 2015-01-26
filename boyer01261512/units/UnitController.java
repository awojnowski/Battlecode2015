package boyer01261512.units;

import java.util.ArrayList;

import battlecode.common.*;
import boyer01261512.*;

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
	
	// MARK: Helpers
	
	private RobotInfo[] militaryUnitsFromUnitArray(RobotInfo[] enemies) {
		
		ArrayList<RobotInfo> militaryEnemies = new ArrayList<RobotInfo>();
		for (RobotInfo enemy : enemies) {

			if (!UnitController.isUnitTypeMilitary(enemy.type)) continue;
			militaryEnemies.add(enemy);
			
		}
		return militaryEnemies.toArray(new RobotInfo[militaryEnemies.size()]);
		
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
	
	public RobotInfo[] nearbyEnemies(RobotType type) throws GameActionException {
		
		return this.nearbyEnemies(type, HQ.type().sensorRadiusSquared);
		
	}
	
	public RobotInfo[] nearbyEnemies(RobotType type, int sensorRadius) throws GameActionException {
		
		ArrayList<RobotInfo> nearbyEnemies = new ArrayList<RobotInfo>();
		
		RobotInfo[] enemies = this.nearbyEnemies(sensorRadius);
		for (RobotInfo enemy : enemies) {
			
			if (enemy.type != type) continue;
			nearbyEnemies.add(enemy);
			
		}
		return nearbyEnemies.toArray(new RobotInfo[nearbyEnemies.size()]);
		
	}
	
	public RobotInfo[] nearbyMilitaryEnemies() throws GameActionException {
		
		return this.nearbyMilitaryEnemies(HQ.type().sensorRadiusSquared);
		
	}
	
	public RobotInfo[] nearbyMilitaryEnemies(int sensorRadius) throws GameActionException {
		
		return this.militaryUnitsFromUnitArray(this.nearbyEnemies(sensorRadius));
		
	}
	
	public RobotInfo[] nearbyEnemiesWithinTheirAttackRange() throws GameActionException {
		
		ArrayList<RobotInfo> dangerousEnemies = new ArrayList<RobotInfo>();
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		RobotInfo[] enemies = this.nearbyEnemies(this.robot.type.sensorRadiusSquared);
		for (RobotInfo enemy : enemies) {
			
			if (enemy.type == Tower.type() || enemy.type == HQ.type()) continue;
			if (currentLocation.distanceSquaredTo(enemy.location) <= Math.pow(Math.sqrt(enemy.type.attackRadiusSquared) + 1, 2)) {

				dangerousEnemies.add(enemy);
				
			}
			
		}
		return dangerousEnemies.toArray(new RobotInfo[dangerousEnemies.size()]);
		
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
		
		return this.nearbyAttackableEnemies(true);
		
	}
	
	public RobotInfo[] nearbyAttackableEnemies(boolean includeMissiles) throws GameActionException {
		
		int attackRadius = this.robot.type.attackRadiusSquared;
		if (this.robot.type == HQ.type()) attackRadius = HQ.friendlyAttackRadiusSquared(this.robot.locationController.enemyTowerLocations().length);
		
		ArrayList<RobotInfo> nearbyEnemies = new ArrayList<RobotInfo>();
		
		RobotInfo[] enemies = this.robot.robotController.senseNearbyRobots(attackRadius, this.robot.team.opponent());
		for (RobotInfo enemy : enemies) {
			
			if (!includeMissiles && enemy.type == Missile.type()) continue;
			nearbyEnemies.add(enemy);
			
		}
		return nearbyEnemies.toArray(new RobotInfo[nearbyEnemies.size()]);
				
	}
	
	public RobotInfo[] nearbyAllies() throws GameActionException {
		
		return this.robot.robotController.senseNearbyRobots(HQ.type().sensorRadiusSquared, this.robot.team);
		
	}
	
	public RobotInfo[] nearbyAllies(int sensorRadius) throws GameActionException {
		
		return this.robot.robotController.senseNearbyRobots(sensorRadius, this.robot.team);
		
	}
	
	public RobotInfo[] nearbyMilitaryAllies() throws GameActionException {
		
		return this.nearbyMilitaryAllies(HQ.type().sensorRadiusSquared);
		
	}
	
	public RobotInfo[] nearbyMilitaryAllies(int sensorRadius) throws GameActionException {
		
		return this.militaryUnitsFromUnitArray(this.nearbyAllies(sensorRadius));
		
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
