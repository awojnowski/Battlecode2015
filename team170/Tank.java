package team170;

import battlecode.common.*;
import java.util.ArrayList;

public class Tank extends BattleRobot {

	public Tank(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			Boolean attacked = this.attack();
			if (this.robotController.isCoreReady()) {
				
				Boolean shouldMove = !attacked;
				if (shouldMove) {
					
					if (!this.shouldMobilize()) {
						
						// figure out if we should engage units in friendly territory
						ArrayList<RobotInfo> targettableEnemies = new ArrayList<RobotInfo>();
						RobotInfo[] enemies = this.unitController.nearbyEnemies(100);
						for (RobotInfo enemy : enemies) {
							
							if (enemy.type == Missile.type()) continue;
							if (locationController.isLocationInFriendlyTerritory(enemy.location)) {
								
								targettableEnemies.add(enemy);
								
							}
							
						}
						
						if (targettableEnemies.size() > 0) {
							
							MapLocation bestLocation = this.desiredEnemy(targettableEnemies.toArray(new RobotInfo[targettableEnemies.size()])).location;
							shouldMove = this.movementController.moveToward(bestLocation) != null;
							
						}
						
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
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.TANK;
	}

}
