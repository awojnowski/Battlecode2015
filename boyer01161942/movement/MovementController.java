package boyer01161942.movement;

import battlecode.common.*;
import boyer01161942.*;
import boyer01161942.units.UnitController;

public class MovementController {
	
	public Robot robot;
		
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
	
	public static Direction directionWithOffset(Direction direction, int offset) {
		return MovementController.directionFromInt(MovementController.directionToInt(direction) + offset);
	}
	
	public static Direction directionFromInt(int d) {
		return DIRECTIONS[directionIndexFromInt(d)];
	}
	
	// moves toward a location using the moveTo method (and its implications)
	public Direction moveToward(MapLocation location) throws GameActionException {
		
		return this.moveToward(location, true);
		
	}
	
	// @param allowGreaterDistance whether or not the robot is allowed to move further away in moving toward
	public Direction moveToward(MapLocation location, Boolean allowGreaterDistance) throws GameActionException {
		
		if (location == null) return null; 
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		double currentDistance = currentLocation.distanceSquaredTo(location);
		
		Direction direction = currentLocation.directionTo(location);
		int directionInteger = directionToInt(direction);
		
		if (moveTo(direction)) {
			
			return direction;
			
		} else {
			
			int[] offsets = {1,2};
			for (int offset : offsets) {

				Direction directionOne = MovementController.directionFromInt(directionInteger + offset);
				Direction directionTwo = MovementController.directionFromInt(directionInteger - offset);
				direction = this.moveTowardDirections(currentLocation, location, directionOne, directionTwo, allowGreaterDistance, currentDistance);
				if (direction != null) return direction;
				
			}
			
		}
		return null;
		
	}
	
	// figures out the better of two directions to go in
	private Direction moveTowardDirections(MapLocation currentLocation, MapLocation desiredLocation, Direction directionOne, Direction directionTwo, Boolean allowGreaterDistance, double currentDistance) throws GameActionException {
		
		Direction direction = null;
		
		MapLocation moveLocationOne = currentLocation.add(directionOne);
		MapLocation moveLocationTwo = currentLocation.add(directionTwo);
		MapLocation moveLocation = null;
		
		int moveLocationInteger = (moveLocationOne.distanceSquaredTo(desiredLocation) <= moveLocationTwo.distanceSquaredTo(desiredLocation)) ? 1 : 2;
		if (moveLocationInteger == 1) {
			
			moveLocation = moveLocationOne;
			direction = directionOne;
			
		} else {
			
			moveLocation = moveLocationTwo;
			direction = directionTwo;
			
		}
		
		if (!allowGreaterDistance && moveLocation.distanceSquaredTo(desiredLocation) > currentDistance) return null;
		if (this.moveTo(direction)) {
			
			return direction;
			
		} else {
			
			if (moveLocationInteger == 1) {
				
				moveLocation = moveLocationTwo;
				direction = directionTwo;
				
			} else {
				
				moveLocation = moveLocationOne;
				direction = directionOne;
				
			}
			if (!allowGreaterDistance && moveLocation.distanceSquaredTo(desiredLocation) > currentDistance) return null;
			if (this.moveTo(direction)) {
				
				return direction;
				
			}
			
		}
		return null;
		
	}
	
	public Direction moveAway(RobotInfo[] enemies) throws GameActionException {
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		MapLocation location = currentLocation;
		for (RobotInfo enemy : enemies) {
			
			location.add(MovementController.directionWithOffset(currentLocation.directionTo(enemy.location), -4), (int)(10 * (currentLocation.distanceSquaredTo(enemy.location) / 35.0)));
			
		}
		return this.moveToward(location);
		
	}
	
	// moves away from a location using the moveTo method (and its implications)
	public Direction moveAway(MapLocation location) throws GameActionException {
		
		if (location == null) return null; 
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		Direction direction = currentLocation.directionTo(location);
		
		return this.moveToward(currentLocation.add(MovementController.directionWithOffset(direction, -4)));
		
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
		
		RobotType type = this.robot.type;

		Boolean moveAroundHQ = !this.robot.currentPlaystyle().canAttackInHQRange(this.robot.unitController.enemyTowers().length);

		Boolean moveAroundTowers = !this.robot.currentPlaystyle().canAttackInTowerRange();
		if (type == Missile.type()) moveAroundTowers = false;
		if (type == Launcher.type()) moveAroundTowers = true;

		Boolean moveAroundMilitary = false;
		if (UnitController.isUnitTypeMiner(type)) moveAroundMilitary = true;
		else if (type == Drone.type()) moveAroundMilitary = true;
		else if (type == Launcher.type()) moveAroundMilitary = true;
		
		if (this.canMoveSafely(direction, moveAroundHQ, moveAroundTowers, moveAroundMilitary)) {
			
			this.robot.robotController.move(direction);
			return true;
			
		}
		return false;
		
	}
    
    public Boolean canMoveSafely(Direction direction, Boolean moveAroundHQ, Boolean moveAroundTowers, Boolean moveAroundUnits) throws GameActionException {
        
        if (direction == null) return false;
    	
    	RobotController rc = this.robot.robotController;
        Boolean canMove = rc.canMove(direction);
        if (!canMove) return false;
        
        MapLocation currentLocation = this.robot.locationController.currentLocation();
        MapLocation moveLocation = currentLocation.add(direction);
        
        if (moveAroundHQ) {

        	MapLocation[] towers = this.robot.unitController.enemyTowers();
            if (moveLocation.distanceSquaredTo(this.robot.locationController.enemyHQLocation()) <= HQ.enemyAttackRadiusSquared(towers.length)) return false;
        	
        }

        if (moveAroundTowers) {
            
            MapLocation[] towers = this.robot.unitController.enemyTowers();
            for (MapLocation tower : towers) {

            	if (moveLocation.distanceSquaredTo(tower) <= Tower.type().attackRadiusSquared) return false;
            	
            }
        	
        }
        
        if (moveAroundUnits) {
        	
        	double buffer = 0;
        	if (this.robot.type == Launcher.type()) buffer = 10;
            
            RobotInfo[] enemies = this.robot.unitController.nearbyEnemies();
            for (RobotInfo enemy : enemies) {

            	this.robot.broadcaster.evaluateSeenLaunchersWithType(enemy.type);
        		if (!UnitController.isUnitTypeDangerous(enemy.type)) continue;
            	if (moveLocation.distanceSquaredTo(enemy.location) <= enemy.type.attackRadiusSquared + buffer) return false;
            	
            }
        	
        }
        
        return true;
    	
    }

}
