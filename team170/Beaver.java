package team170;

import team170.movement.MovementController;
import team170.queue.BuildingQueue;
import team170.units.UnitController;
import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	private BuildingQueue buildingQueue;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.canBeMobilized = false;
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				Boolean builtBuilding = false;
				RobotType buildType = this.currentPlaystyle().nextBuildingType();
				
				// try build the recommended build order building
				
				if (buildType != null) {

					if (this.canBuild(buildType, true))
						builtBuilding = this.tryBuild(buildType, true);
					
				}
				
				// otherwise let's build some other buildings!
				
				if (!builtBuilding) {

					buildType = SupplyDepot.type();
					if (this.canBuild(buildType, false))
						builtBuilding = this.tryBuild(buildType, false);
					
				}
				
				if (!builtBuilding) {
					
					if (this.broadcaster.robotCountFor(Barracks.type()) * 2 >= this.broadcaster.robotCountFor(AerospaceLab.type())) buildType = AerospaceLab.type();
					else buildType = Barracks.type();
					
					if (this.canBuild(buildType, true))
						builtBuilding = this.tryBuild(buildType, true);
					
				}
				
				if (!builtBuilding) {

					final int roundProgress = this.currentPlaystyle().buildOrderProgress();
					if (roundProgress < 3 && this.tryMine()) {
						
						
						
					} else {
						
						MapLocation hqLocation = this.locationController.HQLocation();
						Direction directionToHQ = this.locationController.currentLocation().directionTo(hqLocation);
						Boolean moved = false;
						
						int[] diagonalOffsets = {0, 2, -2, 4, 1, -1, 3, -3};
						int[] cardinalOffsets = {1, -1, 3, -3, 0, 2, -2, 4};
						int[] offsets;
						
						if (directionToHQ.isDiagonal())
							offsets = diagonalOffsets;
						else
							offsets = cardinalOffsets;
						
						for (int i = 0; i < offsets.length && !moved; i++) {
							
							Direction direction = MovementController.directionWithOffset(directionToHQ, offsets[i]);
							if (this.robotController.canMove(direction) && this.shouldMove(direction)) {
								
								this.robotController.move(direction);
								moved = true;
								
							}
							
						}
						
						if (!moved) {
							
							RobotInfo[] nearbyAllies = this.unitController.nearbyAllies(1);
							
							if (nearbyAllies.length > 1)
								this.movementController.moveAway(hqLocation);
							else 
								this.movementController.moveToward(hqLocation);
							
						}
						
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
		
		for (int i = 0; i < 8; i += 2) {
			
			MapLocation adjacentTile = location.add(MovementController.directionFromInt(i));
			RobotInfo robot = this.robotController.senseRobotAtLocation(adjacentTile);
			
			if (robot != null && UnitController.isUnitTypeBuilding(robot.type)) {
				
				adjacentBuildingCount++;
				
			} else if (!this.robotController.senseTerrainTile(adjacentTile).isTraversable()) { // || !canBuildAtLocation(location)) { - uses too many bytecodes
				
				adjacentUnbuildableLocations++;
				
			}
			
		}
		
		return adjacentBuildingCount > 0 && adjacentBuildingCount + adjacentUnbuildableLocations < 4;
		
	}
	
	// MARK: Building
	
	private Boolean canBuild(RobotType type, boolean isCivic) throws GameActionException {
		
		if (type.oreCost > (isCivic ? this.broadcaster.civicBudget() : this.broadcaster.budgetForType(type))) return false;
		if (!this.robotController.hasBuildRequirements(type)) return false;
		return true;
		
	}
	
	private Boolean canBuild(MapLocation location, Direction direction, RobotType type, boolean isCivic) throws GameActionException {

		if (!this.canBuild(type, isCivic)) return false;
		if (!this.robotController.canBuild(direction, type)) return false;
		return this.canBuildAtLocation(location);
		
	}
	
	private Boolean canBuildAtLocation(MapLocation location) throws GameActionException {
		
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
	
	private Boolean tryBuild(RobotType type, boolean isCivic) throws GameActionException {
		
		if (!this.canBuild(type, isCivic)) return false;
		
		MapLocation currentLocation = this.locationController.currentLocation();		
		for (int i = 0; i < 8; i++) {
			
			Direction direction = MovementController.directionFromInt(i);
			MapLocation location = currentLocation.add(direction);
			if (this.canBuild(location, direction, type, isCivic)) {

				this.build(direction, type, isCivic);
				return true;
				
			}
			
		}
		return false;
		
	}
	
	private void build(Direction direction, RobotType type, boolean isCivic) throws GameActionException {
		
		this.robotController.build(direction, type);
		
		if (isCivic)
			this.broadcaster.decrementCivicBudget(type.oreCost);
		else
			this.broadcaster.decrementBudget(type, type.oreCost);
		
		this.broadcaster.beginBuildingRobot(type);
		this.broadcaster.incrementSpentOre(type.oreCost);
				
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
