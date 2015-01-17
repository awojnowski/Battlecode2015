package team170.movement;

import battlecode.common.*;
import team170.*;
import team170.units.UnitController;

public class MovementController {
	
	public Robot robot;
    
	// MARK: Instance Variables
	
    private MapLocation lastPosition;
    private int traversalDirection; // either 1 or -1
    private int turnsStuck = 0;
		
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
        
        int[] offsets = {1,2};
        return this.moveToward(location, allowGreaterDistance, offsets);
       
    }
	
    public Direction moveToward(MapLocation location, Boolean allowGreaterDistance, int[] offsets) throws GameActionException {
           
        if (location == null) return null;
       
        MapLocation currentLocation = this.robot.locationController.currentLocation();
        double currentDistance = currentLocation.distanceSquaredTo(location);
       
        Direction direction = currentLocation.directionTo(location);
        int directionInteger = directionToInt(direction);
       
        if (this.lastPosition != null && currentLocation.distanceSquaredTo(this.lastPosition) <= 1)
            this.turnsStuck++;
               
        if (this.turnsStuck > 1)
            return this.moveAroundObstacleToward(location);
               
        if (moveTo(direction)) {
               
            this.lastPosition = currentLocation;
            return direction;
               
        } else {
               
            for (int offset : offsets) {

                Direction directionOne = MovementController.directionFromInt(directionInteger + offset);
                Direction directionTwo = MovementController.directionFromInt(directionInteger - offset);
                direction = this.moveTowardDirections(currentLocation, location, directionOne, directionTwo, allowGreaterDistance, currentDistance);
                if (direction != null) {
                       
                    this.lastPosition = currentLocation;
                    return direction;
                       
                }
                   
            }
               
        }
       
        this.turnsStuck++;
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
   
    // follows a wall until it can move towards it's initial target, switching directions if it gets stuck
    public Direction moveAroundObstacleToward(MapLocation location) throws GameActionException {
           
        if (this.traversalDirection == 0) { // start with a random traversal direction
               
            if (this.robot.random.nextBoolean())
                this.traversalDirection = -1;
            else
                this.traversalDirection = 1;
               
        }
       
        MapLocation robotLocation = this.robot.locationController.currentLocation();
        Direction directionToTarget = robotLocation.directionTo(location);
        Direction directionToLastPosition = this.lastPosition != null ? robotLocation.directionTo(this.lastPosition) : null;
        int switchedDirection = 0;
       
        this.robot.robotController.setIndicatorString(2, "Traversal Direction " + this.traversalDirection + " Dir to target " + directionToTarget + " Dir to LP " + directionToLastPosition);
       
        // see if it can move toward it's target
        if (directionToTarget == directionToLastPosition || !this.moveTo(directionToTarget)) {
               
            this.robot.robotController.setIndicatorString(2, "I CAN'T move towards target");
            // traverse along obstacle, switching directions if it hits a dead end or outer wall
            for (int i = 1; i < 8  && switchedDirection <= 1; i++) {
                   
                Direction direction = MovementController.directionWithOffset(directionToTarget, i * this.traversalDirection);
                MapLocation nextLocation = robotLocation.add(direction);
                this.robot.robotController.setIndicatorDot(nextLocation, 255, 255, 255);
               
                if (!direction.equals(directionToLastPosition)) {
                       
                    if (this.robot.movementController.moveTo(direction)) {
                           
                        this.lastPosition = robotLocation;
                       
                        return direction;
                           
                    } else {
                   
                        if (this.robot.robotController.senseTerrainTile(nextLocation).equals(TerrainTile.OFF_MAP)) {
                               
                            this.traversalDirection = -this.traversalDirection; // Might need to do additional checks if this switches too often
                            i = 0;
                            switchedDirection++;
                               
                        }
                   
                    }
                       
                }
                   
            }
               
        } else {
               
            this.robot.robotController.setIndicatorString(2, "I can move towards target");
            this.robot.robotController.setIndicatorLine(robotLocation, location, 255, 255, 255);
            this.turnsStuck = 0;
            return directionToTarget;
               
        }
       
        //System.out.println("Didn't find move location.");
        return null;
           
    }
	
	public Direction moveAway(RobotInfo[] enemies) throws GameActionException {
		
		MapLocation currentLocation = this.robot.locationController.currentLocation();
		Direction opposite = null;
		
		for (RobotInfo enemy : enemies) {
			
			opposite = MovementController.directionWithOffset(currentLocation.directionTo(enemy.location), -4);
			break;
			
		}
		return this.moveToward(currentLocation.add(opposite, 40));
		
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

        if (!this.robot.robotController.canMove(direction)) return false;
		
		RobotType type = this.robot.type;
		Boolean moveAroundHQ = !this.robot.canAttackInHQRange(this.robot.unitController.enemyTowers().length);

		Boolean moveAroundTowers = !this.robot.canAttackInTowerRange();
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
