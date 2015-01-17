package team170;

import battlecode.common.*;

public class Tank extends BattleRobot {
		
	// here at ayyyyyyylmao we like verbosity
	private int turnsChasingShitWhenWeShouldBeMobilizing;
	
	public Tank(RobotController robotController) {
		
		super(robotController);
		
		
		
	}

	public void run() {
		
		super.run();
		
		try {
						
			AttackResult attackResult = new AttackResult();
			Boolean attacked = this.attack(attackResult);
			
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked;
				if (shouldMove) {
					
					if (!this.shouldMobilize()) {
						
						if (!this.moveTowardEnemiesInTerritory()) {

							this.move();
							
						}
						
					} else {
						
						if (this.turnsChasingShitWhenWeShouldBeMobilizing < 25 && this.moveTowardEnemiesNearby()) {
							
							this.turnsChasingShitWhenWeShouldBeMobilizing ++;
							
						} else {
							
							if (!this.canAttackInTowerRange()) {
								
								MapLocation towerInRange = this.locationController.enemyTowerInRange();
								if (towerInRange != null) {
									
									this.movementController.fleeFrom(towerInRange);
									shouldMove = false;
									
								}
								
							}
							
							if (shouldMove) {
								
								this.mobilize();
								
								// mobilize for 5 more turns if we were chasing shit
								this.turnsChasingShitWhenWeShouldBeMobilizing ++;
								if (this.turnsChasingShitWhenWeShouldBeMobilizing > 45) {
									
									this.turnsChasingShitWhenWeShouldBeMobilizing = 0;
									
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Movement
	
	private Boolean moveTowardEnemiesInTerritory() throws GameActionException {
		
		return this.moveTowardEnemies(this.enemiesInTerritory());
		
	}
	
	private Boolean moveTowardEnemiesNearby() throws GameActionException { 
		
		return this.moveTowardEnemies(this.nearbyDangerousEnemies(36, 0));
		
	}
	
	private Boolean moveTowardEnemies(RobotInfo[] enemies) throws GameActionException {
		
		if (enemies.length > 0) {
			
			RobotInfo enemy = this.desiredEnemy(enemies);
			MapLocation bestLocation = enemy.location;
			return this.movementController.moveToward(bestLocation) == null;
			
		}
		return false;
		
	}
	
	private void move() throws GameActionException {
		
		MapLocation moveLocation = this.locationController.militaryRallyLocation();
		if (this.locationController.distanceTo(moveLocation) > 18) {

			this.movementController.moveToward(moveLocation);
			
		} else {
			
			this.movementController.moveTo(this.movementController.randomDirection());
			
		}
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TANK;
	}

}
