package boyer01201005;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Basher extends BattleRobot {

	public Basher(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
					
				boolean shouldMove = true;
				if (!this.shouldMobilize()) {
					
					// try to run toward launchers
					RobotInfo[] nearbyLaunchers = this.unitController.nearbyEnemies(Launcher.type());
					if (nearbyLaunchers.length > 0) {

						RobotInfo enemy = this.desiredEnemy(nearbyLaunchers);
						MapLocation bestLocation = enemy.location;
						this.movementController.moveToward(bestLocation);
						shouldMove = false;
						
					}

					// otherwise check the enemies in territory
					RobotInfo[] enemiesInTerritory = this.enemiesInTerritory();
					if (enemiesInTerritory.length > 0) {
						
						RobotInfo enemy = this.desiredEnemy(enemiesInTerritory);
						MapLocation bestLocation = enemy.location;
						shouldMove = this.movementController.moveToward(bestLocation) == null;
						
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
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.BASHER;
	}

}
