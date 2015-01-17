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
			
			this.attack();
			
			Boolean canMove = true;
			if (canMove) {
				
				RobotInfo[] enemiesInRange = this.unitController.nearbyEnemiesWithinTheirAttackRange();
				if (enemiesInRange.length > 0) {
					
					canMove = (this.movementController.moveAway(enemiesInRange) != null);
					
				}
				
			}
			
			if (this.isHarassing()) {
				
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
				
			} else {
				
				if (!this.shouldMobilize()) {
					
					if (canMove) {

						RobotInfo[] enemiesInTerritory = this.enemiesInTerritory();
						if (enemiesInTerritory.length > 0) {
							
							RobotInfo enemy = this.desiredEnemy(enemiesInTerritory);
							MapLocation bestLocation = enemy.location;
							canMove = this.movementController.moveToward(bestLocation) == null;
							
						}
						
					}
					
					// if we haven't moved toward an enemy location then we can go and stick to the plan
					if (canMove) {

						MapLocation rallyLocation = this.locationController.militaryRallyLocation();
						if (this.locationController.distanceTo(rallyLocation) > 18) {

							this.movementController.moveToward(rallyLocation);
							
						} else {
							
							this.movementController.moveTo(this.movementController.randomDirection());
							
						}
						
					}
					
				} else {

					this.mobilize();
					
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
	
	// MARK: Modes
	
	private boolean isHarassing() {
		
		return (Clock.getRoundNum() < 800);
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.COMMANDER;
	}

}
