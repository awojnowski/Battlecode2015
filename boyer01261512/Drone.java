package boyer01261512;

import battlecode.common.*;

import java.util.ArrayList;
import boyer01261512.units.*;

public class Drone extends BattleRobot {
	
	private ArrayList<RobotInfo> locations;

	public Drone(RobotController robotController) {
		
		super(robotController);
		
		this.locations = new ArrayList<RobotInfo>();
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isWeaponReady()) {
				
				RobotInfo enemy = this.unitController.closestNearbyEnemy();
				if (enemy != null) {
					
					if (this.locationController.distanceTo(enemy.location) < this.type.attackRadiusSquared &&
						(enemy.type == Missile.type() || enemy.type == Miner.type()))  {
							
						this.robotController.attackLocation(enemy.location);
							
					}
					
				}
				
			}

			if (this.robotController.isCoreReady()) {
				
				boolean moved = false;
				RobotInfo closestEnemy = this.unitController.closestMilitaryAttackerWithinRange();
				if (closestEnemy != null) {
					
					moved = this.movementController.fleeFrom(closestEnemy.location) != null;
					
				}
				
				if (!moved) {
					
					if (this.robotController.getSupplyLevel() > 500) {
						
						if (this.locations.size() == 0) {
							
							// figure out some locations to go to
							this.refreshLocations();
							
						}
						
						// move to the locations to distribute supply
						
						if (this.locations.size() > 0) {
							
							MapLocation nextLocation = this.locations.get(0).location;
							if (this.locationController.distanceTo(nextLocation) < GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED / 2) {
								
								this.locations.remove(0);
								
							} else {
								
								this.movementController.moveToward(nextLocation);
								
							}
							
						}
						
					} else {
						
						this.locations.clear();
						
						// go back to the HQ
						MapLocation HQLocation = this.locationController.HQLocation();
						if (this.locationController.distanceTo(HQLocation) > GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED) {
							
							this.movementController.moveToward(HQLocation);
							
						}
						
					}
					
				}
				
			}

			this.supplyController.transferSupplyIfPossible();
			
			if (this.locations.size() > 0) {
				
				this.robotController.setIndicatorLine(this.locations.get(0).location, this.robotController.getLocation(), 255, 255, 0);
				
			}
			
		} catch (GameActionException exception) {
			
			
			
		}
		
		this.robotController.yield();
		
	}
	
	public Boolean canMoveInHQRange(int totalTowers) throws GameActionException {
		
		return false;
		
	}
	
	public Boolean canMoveInTowerRange() throws GameActionException {

		return false;
		
	}
	
	public Boolean canMoveInMilitaryRange() throws GameActionException {
		
		return false;
		
	}
	
	// MARK: Location Helpers
	
	private void refreshLocations() throws GameActionException {
		
		RobotInfo[] friendlies = this.unitController.nearbyAllies(100000);
		for (RobotInfo friendly : friendlies) {
			
			if (UnitController.isUnitTypeMilitary(friendly.type)) {
				
				this.attemptToAddRobotToLocations(friendly);
				
			}
			
		}
		
		for (RobotInfo friendly : friendlies) {
			
			if (friendly.type == Miner.type()) {
				
				this.attemptToAddRobotToLocations(friendly);
				
			}
			
		}
		
	}
	
	private void attemptToAddRobotToLocations(RobotInfo robot) {
		
		if (robot.supplyLevel > 0) return;
		
		boolean isLocationValid = true;
		for (RobotInfo info : this.locations) {
			
			if (robot.location.distanceSquaredTo(info.location) < 40) {
				
				isLocationValid = false;
				break;
				
			}
			
		}
		
		if (isLocationValid) {
			
			this.locations.add(robot);
			
		}
		
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.DRONE;
	}

}
