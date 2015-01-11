package boyer01101154.movement;

import battlecode.common.*;
import boyer01101154.*;
import boyer01101154.units.UnitController;

public class MovementController {
	
	public Robot robot;
	
	private int stopTurns;
	
	// MARK: Directions
	
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

	// MARK: Movement

	public Boolean shouldMove() {
		
		return this.stopTurns == 0;
		
	}
	
	// moves toward a location using the moveTo method (and its implications)
	public Direction moveToward(MapLocation location) throws GameActionException {
		
		if (location == null) return null; 
		if (!this.shouldMove()) return null;
		
		Direction direction = this.robot.robotController.getLocation().directionTo(location);
		int directionInteger = this.directionToInt(direction);
		
		int[] offsets = {0,1,-1,2,-2};
		for (int offset : offsets) {
			
			direction = this.robot.locationController.DIRECTIONS[(directionInteger + offset + 8) % 8];
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
		int directionInteger = this.directionToInt(direction);
		
		int[] offsets = {-4,5,-5,6,-6};
		for (int offset : offsets) {
			
			direction = this.robot.locationController.DIRECTIONS[(directionInteger + offset + 8) % 8];
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
		int directionInteger = this.directionToInt(direction);
		
		int[] offsets = {-4,5,-5,6,-6};
		for (int offset : offsets) {
			
			direction = this.robot.locationController.DIRECTIONS[(directionInteger + offset + 8) % 8];
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
		if (type == Drone.type()) moveAroundMilitary = true;
		
		Boolean canMove = null;
		if (this.robot.currentPlaystyle().canAttackInTowerRange()) canMove = this.robot.robotController.canMove(direction);
		else canMove = this.canMoveSafely(direction, moveAroundMilitary);
		
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
        
        if (moveLocation.distanceSquaredTo(this.robot.locationController.enemyHQLocation()) <= HQ.type().attackRadiusSquared + 1) return false;
        
        return true;
    	
    }
    
	// MARK: Move Turns
	
	public void decrementMoveTurns() {
	
		this.stopTurns = Math.max(0, this.stopTurns - 1);
	
	}

}
