package team170.movement;

import battlecode.common.*;
import team170.*;
import team170.units.UnitController;

public class MovementController {
	
	public Robot robot;
	
	private int stopTurns;
	
	// MARK: Static Variables
	
	public static final Direction[] DIRECTIONS = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	
	// MARK: Directions
	
	public Direction randomDirection() {
		
		int rand = this.robot.random.nextInt(DIRECTIONS.length);
		return DIRECTIONS[rand];
		
	}
	
	public static int directionToInt(Direction d) {
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
	
	public static int directionIndexFromInt(int d) {
		return (d + 8000) % 8;
	}
	
	public static Direction directionFromInt(int d) {
		return DIRECTIONS[directionIndexFromInt(d)];
	}

	// MARK: Movement

	public Boolean shouldMove() {
		
		return this.stopTurns == 0;
		
	}
	
	// moves toward a location using the moveTo method (and its implications)
	public Direction moveToward(MapLocation location) throws GameActionException {
		
		return this.moveToward(location, true);
		
	}
	
	// @param allowGreaterDistance whether or not the robot is allowed to move further away in moving toward
	public Direction moveToward(MapLocation location, Boolean allowGreaterDistance) throws GameActionException {
		
		if (location == null) return null; 
		if (!this.shouldMove()) return null;
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		double currentDistance = currentLocation.distanceSquaredTo(location);
		
		Direction direction = this.robot.robotController.getLocation().directionTo(location);
		int directionInteger = directionToInt(direction);
		
		int[] offsets = {0,1,-1,2,-2};
		for (int offset : offsets) {
			
			direction = MovementController.directionFromInt(directionInteger + offset);
			MapLocation moveLocation = currentLocation.add(direction);
			if (!allowGreaterDistance && moveLocation.distanceSquaredTo(location) > currentDistance) continue;
			
			if (this.moveTo(direction)) {
				
				return direction;
				
			}
			
		}
		return null;
		
	}
	
	// moves away from a location using the moveTo method (and its implications)
	public Direction moveAway(MapLocation location) throws GameActionException {
		
		if (location == null) return null; 
		if (!this.shouldMove()) return null;
		
		Direction direction = this.robot.robotController.getLocation().directionTo(location);
		int directionInteger = directionToInt(direction);
		
		int[] offsets = {-4,5,-5,6,-6};
		for (int offset : offsets) {
			
			direction = DIRECTIONS[(directionInteger + offset + 8) % 8];
			if (this.moveTo(direction)) {
				
				return direction;
				
			}
			
		}
		return null;
		
	}
	
	// moves away from a location with no regard for its own safety aka gtfo pce
	public Boolean fleeFrom(MapLocation location) throws GameActionException {
		
		if (location == null) return false; 
		
		Direction direction = this.robot.robotController.getLocation().directionTo(location);
		int directionInteger = directionToInt(direction);
		
		int[] offsets = {-4,5,-5,6,-6};
		for (int offset : offsets) {
			
			direction = DIRECTIONS[(directionInteger + offset + 8) % 8];
			if (this.robot.robotController.canMove(direction)) {
				
				this.robot.robotController.move(direction);
				return true;
				
			}
			
		}
		return false;
		
	}
	
	// attempts to move toward a location
	// depending on the playstyle and unit type, it will avoid towers or other unit types
	public Boolean moveTo(Direction direction) throws GameActionException {
		
		if (!this.shouldMove()) return false;

		RobotType type = this.robot.type;

		Boolean moveAroundMilitary = false;
		if (UnitController.isUnitTypeMiner(type)) moveAroundMilitary = true;
		else if (type == Drone.type()) moveAroundMilitary = true;

		Boolean moveSafely = false;
		if (type == Missile.type()) moveSafely = false;
		if (type == Launcher.type()) moveSafely = true;
		else moveSafely = !this.robot.currentPlaystyle().canAttackInTowerRange();
		
		Boolean canMove = null;
		if (moveSafely) canMove = this.canMoveSafely(direction, moveAroundMilitary);
		else canMove = this.robot.robotController.canMove(direction);
		
		if (canMove) {
			
			this.robot.robotController.move(direction);
			return true;
			
		}
		return false;
		
	}
	
	public void stopFor(int turns) {
		
		this.stopTurns = turns;
		
	}
    
    public Boolean canMoveSafely(Direction direction, Boolean moveAroundUnits) throws GameActionException {
        
        if (direction == null) return false;
    	
    	RobotController rc = this.robot.robotController;
        Boolean canMove = rc.canMove(direction);
        if (!canMove) return false;
        
        MapLocation currentLocation = this.robot.locationController.currentLocation();
        MapLocation moveLocation = currentLocation.add(direction);
        
        if (moveAroundUnits) {
            
            RobotInfo[] enemies = this.robot.unitController.nearbyEnemies();
            for (RobotInfo enemy : enemies) {

        		if (!UnitController.isUnitTypeDangerous(enemy.type)) continue;
            	if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared) return false;
            	
            }
        	
        }
        
        MapLocation[] towers = this.robot.unitController.enemyTowers();
        for (MapLocation tower : towers) {

        	if (moveLocation.distanceSquaredTo(tower) <= Tower.type().attackRadiusSquared) return false;
        	
        }
        
        if (moveLocation.distanceSquaredTo(this.robot.locationController.enemyHQLocation()) <= HQ.attackRadiusSquared(towers.length)) return false;
        
        return true;
    	
    }
    
	// MARK: Move Turns
	
	public void decrementMoveTurns() {
	
		this.stopTurns = Math.max(0, this.stopTurns - 1);
	
	}

}
