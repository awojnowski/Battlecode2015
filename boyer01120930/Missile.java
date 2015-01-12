package boyer01120930;

import battlecode.common.*;

public class Missile extends Robot {
	
	private Direction direction;

	public Missile(RobotController robotController) {
		
		super(robotController);
		
		try {
			
			this.direction = this.broadcaster.nextMissileDirection();
			
		}
		catch (GameActionException e) {
			
			this.direction = this.movementController.randomDirection();
			
		}
		
	}

	public void run() {
		
		super.run();
		
		try {

			if (this.robotController.canMove(this.direction)) {
				
				this.robotController.move(this.direction);
				
			}
			
		}
		catch (GameActionException e) {}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.MISSILE;
	}

}
