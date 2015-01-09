package team170;

import battlecode.common.*;

public class Miner extends BattleRobot {
	
	public Direction facing;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				MapLocation enemyLocation = null;
				
				RobotInfo[] enemies = this.enemies();
				for (RobotInfo enemy : enemies) {
					
					double distance = this.locationController.distanceTo(enemy.location);
					if (distance <= enemy.type.attackRadiusSquared /* constant */) {
						
						enemyLocation = enemy.location;
						break;
						
					}
					
				}
				
				if (enemyLocation != null) {

					Direction direction = this.movementController.moveAway(enemyLocation);
					if (direction != null) this.facing = direction;
					
				} else {
					
					if (!this.tryMine(false)) { 
						
						// no ore underneath
						
						while (!this.robotController.canMove(this.facing)) {
								
							this.facing = this.locationController.randomDirection();
								
						}
						this.movementController.moveTo(facing);
						
						
					}
					
				}
				
			}
			
			this.transferSupplyIfPossible();
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINER;
	}

}
