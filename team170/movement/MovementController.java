package team170.movement;

import battlecode.common.*;
import team170.*;

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
	
	public Boolean moveTo(Direction direction) throws GameActionException {
		
		if (!this.shouldMove()) return false;
		
		if (this.robot.robotController.canMove(direction)) {
			
			this.robot.robotController.move(direction);
			return true;
			
		}
		return false;
		
	}
	
	public void stopFor(int turns) {
		
		this.stopTurns = turns;
		
	}
	
	// MARK: Move Turns
	
	public void decrementMoveTurns() {
	
		this.stopTurns = Math.max(0, this.stopTurns - 1);
	
	}

}
