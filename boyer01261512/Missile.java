package boyer01261512;

import battlecode.common.*;
import boyer01261512.broadcaster.Broadcaster;
import boyer01261512.movement.*;

public class Missile extends Robot {
	
	private Direction direction;
	private int turns = 0;

	public Missile(RobotController robotController) {
		
		super(robotController, false);
		
		this.broadcaster = new Broadcaster();
		this.broadcaster.robotController = robotController;
		this.robotController = robotController;
		
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
			
			if (this.turns > 0) {

				final int travelDistance = (int)Math.pow(5 - this.turns, 2);
				
				MapLocation closest = null;
				double distance = Double.MAX_VALUE;
				
				MapLocation currentLocation = this.robotController.getLocation();
				
				RobotInfo[] enemies = this.robotController.senseNearbyRobots(24, this.robotController.getTeam().opponent());
				if (enemies.length == 0) this.robotController.disintegrate();
				
				for (RobotInfo enemy : enemies) {
					
					if (enemy.type == RobotType.MISSILE) continue;
					
					double enemyDistance = currentLocation.distanceSquaredTo(enemy.location);
					
					// we want to prioritize towers
					if ((enemy.type == RobotType.TOWER || enemy.type == RobotType.HQ) && distance <= travelDistance) {

						closest = enemy.location;
						distance = enemyDistance;
						break;
						
					}
					
					// otherwise see if it is closest
					if (enemyDistance < distance) {
						
						closest = enemy.location;
						distance = enemyDistance;
						
					}
					
				}
				
				if (distance <= 2) {
					
					this.robotController.explode();
					return;
					
				}
				
				if (closest == null) { 

					closest = this.robotController.senseEnemyHQLocation();
					
				}
				this.direction = currentLocation.directionTo(closest);
				
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
		
		this.turns ++;
		this.robotController.setIndicatorString(1, "Turn: " + this.turns);
		
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
