package team170;

import team170.movement.MovementController;
import team170.units.UnitController;
import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	public Direction facing;
	public Boolean isDesignatedBuilder;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.canBeMobilized = false;
		this.facing = this.movementController.randomDirection();
		this.isDesignatedBuilder = true;
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.isDesignatedBuilder && this.robotController.getHealth() > 10) {
					
					Boolean builtBuilding = false;
					RobotType buildType = this.currentPlaystyle().nextBuildingType();
					if (buildType != null) {
						
						builtBuilding = this.tryBuild(buildType);
						
					}
					
					// try to build a supply depot
					if (!builtBuilding) {
						
						this.tryBuild(SupplyDepot.type());
						
					}
					
				}
				
				if (this.tryMine(false)) {
					

					
				} else { // no ore underneath
					
					if (this.isDesignatedBuilder) {

						MapLocation hqLocation = this.locationController.HQLocation();
						if (this.locationController.distanceTo(hqLocation) > 81) {

							this.movementController.moveToward(hqLocation);
							
						} else {
							
							this.movementController.moveTo(this.movementController.randomDirection());
							
						}
						
					} else {
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Building
	
	public Boolean canBuild(MapLocation location, Direction direction, RobotType type) throws GameActionException {
		
		if (type.oreCost > this.broadcaster.budgetForType(type)) return false;
		if (!this.robotController.hasBuildRequirements(type)) return false;
		if (!this.robotController.canBuild(direction, type)) return false;
		
		Boolean edgesHaveBuilding = false;
		Boolean cornersHaveBuilding = false;
		
		for (int i = 0; i < 8 && !edgesHaveBuilding; i++) {
			
			MapLocation adjacentTile = location.add(MovementController.directionFromInt(i));
			RobotInfo robot = this.robotController.senseRobotAtLocation(adjacentTile);
			if (i % 2 != 0) {

				if (robot == null) continue;
				if (UnitController.isUnitTypeBuilding(robot.type)) {
					
					cornersHaveBuilding = true;
					
				}
				
			} else {

				if (robot == null) continue;
				if (UnitController.isUnitTypeBuilding(robot.type)) {
					
					edgesHaveBuilding = true;
					
				}
				
			}
			
		}
		return (cornersHaveBuilding && !edgesHaveBuilding);
		
	}
	
	public Boolean tryBuild(RobotType type) throws GameActionException {
		
		MapLocation currentLocation = this.locationController.currentLocation();		
		for (int i = 0; i < 8; i++) {
			
			Direction direction = MovementController.directionFromInt(i);
			MapLocation location = currentLocation.add(direction);
			if (this.canBuild(location, direction, type)) {

				this.build(direction, type);
				return true;
				
			}
			
		}
		return false;
		
	}
	
	public void build(Direction direction, RobotType type) throws GameActionException {
		
		this.robotController.build(direction, type);
		this.broadcaster.decrementBudget(type, type.oreCost);
		this.broadcaster.beginBuildingRobot(type);
		this.broadcaster.incrementSpentOre(type.oreCost);
				
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
