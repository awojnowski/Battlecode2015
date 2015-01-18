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
			
			this.robotController.setIndicatorString(1, "Turn: " + this.currentPlaystyle().buildOrderProgress() + " Budget: Civic = " + this.broadcaster.civicBudget() + " Supply Depot = " + this.broadcaster.budgetForType(SupplyDepot.type()));
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.isDesignatedBuilder && this.robotController.getHealth() > 10) {
					
					Boolean builtBuilding = false;
					RobotType buildType = this.currentPlaystyle().nextBuildingType();
					if (buildType != null) {
						
						builtBuilding = this.tryBuild(buildType);
						
					}
					
					// try to build some other things
					if (!builtBuilding) {

						builtBuilding = this.tryBuild(SupplyDepot.type());
						
					}
					
				}
				
				if (this.tryMine(false)) {
					
					
					
				} else { // no ore underneath
					
					if (this.isDesignatedBuilder) {
							
						MapLocation hqLocation = this.locationController.HQLocation();
						Direction directionToHQ = this.locationController.currentLocation().directionTo(hqLocation);
						Boolean moved = false;
						
						int[] offsets = {0, 1, -1, 2, -2, 3, -3, 4};
						
						for (int i = 0; i < offsets.length && !moved; i++) {
							
							Direction direction = MovementController.directionWithOffset(directionToHQ, offsets[i]);
							if (this.robotController.canMove(direction) && this.shouldMove(direction)) {
								
								this.robotController.move(direction);
								moved = true;
								
							}
							
						}
						
						if (!moved) {
							
							if (this.locationController.distanceTo(hqLocation) > 81) {
	
								this.movementController.moveToward(hqLocation);
								
							} else {
								
								this.movementController.moveTo(this.movementController.randomDirection());
								
							}
							
						}
						
					} else {
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible(false);
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// Checks positions around potential location and sees if it can build if it moved there
	private Boolean shouldMove(Direction direction) throws GameActionException {
		
		MapLocation location = this.locationController.currentLocation().add(direction);
		
		int adjacentBuildingCount = 0;
		int adjacentUnbuildableLocations = 0;
		
		for (int i = 0; i < 8; i++) {
			
			MapLocation adjacentTile = location.add(MovementController.directionFromInt(i));
			RobotInfo robot = this.robotController.senseRobotAtLocation(adjacentTile);
			
			if (i % 2 == 0) {

				if (robot != null && UnitController.isUnitTypeBuilding(robot.type)) {
					
					adjacentBuildingCount++;
					
				} else if (!this.robotController.senseTerrainTile(adjacentTile).isTraversable()) { // || !canBuildAtLocation(location)) { - uses too many bytecodes
					
					adjacentUnbuildableLocations++;
					
				}
				
			}
			
		}
		
		return adjacentBuildingCount > 0 && adjacentBuildingCount + adjacentUnbuildableLocations < 4;
		
	}
	
	// MARK: Building
	
	public Boolean canBuild(MapLocation location, Direction direction, RobotType type) throws GameActionException {
		
		if (type.oreCost > this.broadcaster.budgetForType(type)) return false;
		if (!this.robotController.hasBuildRequirements(type)) return false;
		if (!this.robotController.canBuild(direction, type)) return false;
		
		return this.canBuildAtLocation(location);
		
	}
	
	private Boolean canBuildAtLocation (MapLocation location) throws GameActionException {
		
		Boolean edgesHaveBuilding = false;
		Boolean cornersHaveBuilding = false;
		
		for (int i = 0; i < 8 && !edgesHaveBuilding; i++) {
			
			MapLocation adjacentTile = location.add(MovementController.directionFromInt(i));
			RobotInfo robot = this.robotController.senseRobotAtLocation(adjacentTile);
			if (i % 2 != 0) {

				if (robot == null) continue;
				if (UnitController.isUnitTypeBuilding(robot.type) && robot.type != Tower.type()) {
					
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
