package team170;

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
		
		//super.run();
		
		try {
			
			RobotInfo closest = null;
			double distance = Double.MAX_VALUE;
			
			MapLocation currentLocation = this.robotController.getLocation();
			RobotInfo[] enemies = this.robotController.senseNearbyRobots(24, this.robotController.getTeam().opponent());
			for (RobotInfo enemy : enemies) {
				
				if (enemy.type == RobotType.MISSILE) continue;
				
				double enemyDistance = currentLocation.distanceSquaredTo(enemy.location);
				if (enemyDistance < distance) {
					
					closest = enemy;
					distance = enemyDistance;
					
				}
				
			}
			
			if (closest != null) { 

				this.direction = this.locationController.currentLocation().directionTo(closest.location);
				
			}

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
