package tulsi;

import battlecode.common.*;

import java.util.Random;

public class Robot {
	
	// MARK: Static Variables
	
	private static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	// MARK: Instance Variables
	
	// controllers
	public RobotBroadcaster broadcaster;
	public RobotController robotController;
	public Random random;
	
	// helpers
	public Team team;
	public RobotType type;
	
	// MARK: Main Methods
	
	public Robot(RobotController robotController) {
		
		this.broadcaster = new RobotBroadcaster();
		this.robotController = robotController;
		this.random = new Random(robotController.getID());
		
		// update the controllers
		
		this.broadcaster.robotController = this.robotController;
		
		// setup the helpers
		
		this.team = this.robotController.getTeam();
		this.type = this.robotController.getType();
		
	}
	
	public void run() {
		
		;
		
	}
	
	// MARK: Building
	
	public Boolean canBuild(Direction direction, RobotType type) {
		
		if (this.robotController.hasBuildRequirements(type)) {
			
			if (this.robotController.canBuild(direction, type)) {

				return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean tryBuild(Direction direction, RobotType type) throws GameActionException {
		
		if (this.canBuild(direction, type)) {
			
			this.build(direction, type);
			return true;
			
		}
		return false;
		
	}
	
	public void build(Direction direction, RobotType type) throws GameActionException {
		
		this.robotController.build(direction, type);
		
	}
	
	// MARK: Directions
	
	public Direction randomDirection() {
		
		int rand = this.random.nextInt(directions.length);
		return directions[rand];
		
	}
	
	public int directionToInt(Direction d) {
		switch(d) {
			case NORTH:
				return 0;
			case NORTH_EAST:
				return 1;
			case EAST:
				return 2;
			case SOUTH_EAST:
				return 3;
			case SOUTH:
				return 4;
			case SOUTH_WEST:
				return 5;
			case WEST:
				return 6;
			case NORTH_WEST:
				return 7;
			default:
				return -1;
		}
	}
	
	// MARK: Distance
	
	public int distanceTo(MapLocation location) {
		
		return this.robotController.getLocation().distanceSquaredTo(location);
		
	}
	
	// MARK: Locations
	
	public MapLocation[] towerLocations() {
		
		return this.robotController.senseTowerLocations();
		
	}
	
	public MapLocation HQLocation() {
		
		return this.robotController.senseHQLocation();
		
	}
	
	// MARK: Locations (Enemy)

	public MapLocation[] enemyTowerLocations() {
		
		return this.robotController.senseEnemyTowerLocations();
		
	}
	
	public MapLocation closestEnemyTower() { 
		
		MapLocation closestTower = null;
		int closestDistance = Integer.MAX_VALUE;
		
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = this.distanceTo(tower);
			if (distance < closestDistance) {
				
				closestTower = tower;
				closestDistance = distance;
				
			}
			
		}
		return closestTower;
		
	}
	
	public MapLocation enemyHQLocation() {
		
		return this.robotController.senseEnemyHQLocation();
		
	}
	
	// MARK: Movement
	
	public void moveToward(MapLocation location) throws GameActionException {
		
		Direction direction = this.robotController.getLocation().directionTo(location);
		int directionInteger = this.directionToInt(direction);
		
		int[] offsets = {0,1,-1,2,-2};
		for (int offset : offsets) {
			
			direction = directions[(directionInteger + offset + 8) % 8];
			if (this.moveTo(direction)) return;
			
		}
		
	}
	
	public Boolean moveTo(Direction direction) throws GameActionException {
		
		if (this.robotController.canMove(direction)) {
			
			this.robotController.move(direction);
			return true;
			
		}
		return false;
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 0;
	}
	
	public static RobotType type() {
		return RobotType.HQ;
	}

}
