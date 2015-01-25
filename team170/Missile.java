package team170;

import battlecode.common.*;
import team170.movement.*;

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
			
			if (distance <= 2) {
				
				this.robotController.explode();
				return;
				
			}
			
			if (closest != null) { 

				this.direction = this.locationController.currentLocation().directionTo(closest.location);
				
			}
			
			if (this.robotController.canMove(this.direction)) {
				
				this.robotController.move(this.direction);
				
			} else if (this.robotController.canMove(MovementController.directionWithOffset(this.direction, -1))) {
				
				this.robotController.move(MovementController.directionWithOffset(this.direction, -1));
				
			} else if (this.robotController.canMove(MovementController.directionWithOffset(this.direction, 1))) {
				
				this.robotController.move(MovementController.directionWithOffset(this.direction, 1));
				
			}
			
		}
		catch (GameActionException e) {}
		
		this.robotController.yield();
		
	}
	
	public Boolean canMoveInTowerRange() throws GameActionException {

		return true;
		
	}
	
	public Boolean canMoveInHQRange(int totalTowers) throws GameActionException {
		
		return true;
		
	}
	
	public Boolean canMoveInMilitaryRange() throws GameActionException {
		
		return true;
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.MISSILE;
	}

}
