package boyer01120930;

import battlecode.common.*;

import java.util.ArrayList;

public class Tank extends BattleRobot {
	
	private int turnsChasingDrone;
	private int freezeTurns = 0;

	public Tank(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			this.robotController.setIndicatorString(2, "");
			
			AttackResult attackResult = new AttackResult();
			Boolean attacked = this.attack(attackResult);
			if (attackResult.target != null) {
								
				if (attackResult.target.type == Drone.type()) {
					
					// don't want to be kited by drones
					this.freezeTurns = 10;
					this.turnsChasingDrone = 0; // since we attacked it
					
				}
				
			}
			
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked && this.freezeTurns == 0;
				if (shouldMove) {
					
					RobotInfo dangerousEnemy = this.nearbyDangerousEnemy();
					if (dangerousEnemy != null) {
						
						this.movementController.moveAway(dangerousEnemy.location);
						
					}
					
				}
				
				if (shouldMove) {
					
					if (!this.shouldMobilize()) {

						RobotInfo[] enemiesInTerritory = this.enemiesInTerritory();
						if (enemiesInTerritory.length > 0) {
							
							RobotInfo enemy = this.desiredEnemy(enemiesInTerritory);
							MapLocation bestLocation = enemy.location;
							shouldMove = this.movementController.moveToward(bestLocation) == null;
							if (!shouldMove) {
								
								// avoid chasing drones
								if (enemy.type == Drone.type()) {
									
									this.turnsChasingDrone ++;
									if (this.turnsChasingDrone > 3) {
										
										this.freezeTurns = 20;
										this.turnsChasingDrone = 0;
										
									}
									
								} else {
									
									this.turnsChasingDrone = 0;
									
								}
								
							}
							
						}
						
						// if we haven't moved toward an enemy location then we can go and stick to the plan
						if (shouldMove) {

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
				
			}
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		if (this.freezeTurns > 0) this.freezeTurns --;
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TANK;
	}

}
