package tulsi;

import battlecode.common.*;

import java.util.Random;

public class Robot {
	
	// MARK: Static Variables
	
	private static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	// MARK: Instance Variables
	
	public RobotBroadcaster broadcaster;
	public RobotController robotController;
	public Random random;
	
	// MARK: Main Methods
	
	public Robot(RobotController robotController) {
		
		this.broadcaster = new RobotBroadcaster();
		this.robotController = robotController;
		this.random = new Random(robotController.getID());
		
	}
	
	public void run() {
		
		;
		
	}
	
	// MARK: Directions
	
	public Direction randomDirection() {
		
		int rand = this.random.nextInt(directions.length);
		return directions[rand];
		
	}
	
	// MARK: Movement
	
	public void moveTo(Direction direction) throws GameActionException {
		
		if (this.robotController.canMove(direction)) {
			
			this.robotController.move(direction);
			
		}
		
	}

}
