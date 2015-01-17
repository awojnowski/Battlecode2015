package team170;

import team170.movement.MovementController;
import battlecode.common.*;

public class Commander extends BattleRobot {
	
	private MapLocation moveAwayLocation = null;
	
	public Commander(RobotController robotController) {
		
		super(robotController);
				
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.isHarassing()) {
				
				this.attack();
				
				Boolean canMove = true;
				if (canMove) {
					
					RobotInfo[] enemiesInRange = this.unitController.nearbyEnemiesWithinTheirAttackRange();
					if (enemiesInRange.length > 0) {
						
						this.moveAway(enemiesInRange);
						canMove = false;
						
					}
					
				}
				
				if (this.moveAwayLocation != null) {
					
					this.robotController.setIndicatorLine(this.robotController.getLocation(), this.moveAwayLocation, 255, 255, 255);
					this.robotController.setIndicatorString(1, "MOVING AWAY");
					
				}
				
				if (canMove) {
					
					if (this.canFlash() && this.robotController.getFlashCooldown() == 0) {
						
						MapLocation currentLocation = this.locationController.currentLocation();
						Direction direction = currentLocation.directionTo(this.locationController.enemyHQLocation());
						this.robotController.castFlash(currentLocation.add(direction, 2));
						
					} else {

						canMove = (this.movementController.moveToward(this.locationController.enemyHQLocation()) != null);
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Getters & Setters
	
	private boolean canFlash() {
		
		return false;
		
	}
	
	// MARK: Kiting
	
	public void moveAway(RobotInfo[] enemies) throws GameActionException {
		
		MapLocation currentLocation = this.locationController.currentLocation();
		Direction opposite = null;
		
		for (RobotInfo enemy : enemies) {
			
			opposite = MovementController.directionWithOffset(currentLocation.directionTo(enemy.location), -4);
			break;
			
		}
		this.moveAwayLocation = currentLocation.add(opposite, 20);
		this.movementController.moveTo(opposite);
		
	}
	
	// MARK: Modes
	
	private boolean isHarassing() {
		
		return (Clock.getRoundNum() < 2000);
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.COMMANDER;
	}

}
