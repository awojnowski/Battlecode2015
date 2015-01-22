package boyer01201005;

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
			
			if (this.robotController.isWeaponReady()) {
				
				AttackResult attackResult = new AttackResult();
				this.attack(attackResult);
				
			}
			
			if (this.robotController.isCoreReady()) {
				
				RobotInfo[] nearbyAttackableEnemies = this.unitController.nearbyAttackableEnemies(true); 
				if (nearbyAttackableEnemies.length == 0 || this.currentPlaystyle().shouldBlitzkrieg()) {
					
					// only move if we have no enemies to 
					if (!this.shouldMobilize()) {
						
						if (!this.moveTowardEnemiesInTerritory()) {

							this.move();
							
						}
						
					} else {
						
						if (this.turnsChasingShitWhenWeShouldBeMobilizing < 25 && this.moveTowardEnemiesNearby()) {
							
							this.turnsChasingShitWhenWeShouldBeMobilizing ++;
							
						} else {
							
							boolean shouldMove = true;
							if (!this.canAttackInTowerRange()) {
								
								MapLocation towerInRange = this.locationController.enemyTowerInRange();
								if (towerInRange != null) {
									
									this.movementController.fleeFrom(towerInRange);
									shouldMove = false;
									
								}
								
							}
							
							if (shouldMove) {
								
								MapLocation objective = this.locationController.bestObjective();
								if (objective != null) {

									// check to see if we have a formation around it
									if (this.locationController.currentLocation().distanceSquaredTo(objective) > 10) {

										this.mobilize();
										
									}
									
								} else {
									
									this.mobilize();
									
								}
								
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
			return this.movementController.moveToward(enemy.location) == null;
			
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
