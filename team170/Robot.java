package team170;

import battlecode.common.*;
import java.util.Random;
import team170.playstyles.*;

public abstract class Robot {
	
	private static final int MAX_BUILD_TILES = 10000;
	
	// MARK: Static Variables
	
	private static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	// MARK: Instance Variables
	
	// controllers
	public RobotBroadcaster broadcaster;
	public RobotController robotController;
	public Random random;
	
	// helpers
	public Team team;
	public RobotType type;
	
	// movement
	public int stopTurns;
	
	// playstyles
	private Playstyle playstyle;
	
	// turns
	private int turns;
	
	// MARK: Main Methods
	
	public Robot(RobotController robotController) {
		
		this.broadcaster = new RobotBroadcaster();
		this.robotController = robotController;
		this.random = new Random(robotController.getID());
		
		// update the controllers
		
		this.broadcaster.robotController = this.robotController;
		
		// setup the helpers
		
		this.team = this.robotController.getTeam();
		this.type = this.robotController.getType();
		
	}
	
	public void run() {

		try {
			this.broadcaster.incrementLivingTalliedRobotCountFor(this.type);
			this.stopTurns = Math.max(this.stopTurns - 1, 0);
			this.turns ++;
			
			// bug in that we have to decrement the building robot count on the second turn (not the first turn)
			if (this.turns == 2) {

				this.broadcaster.decrementBuildingRobotCountFor(this.type);
				
			}
		}
		catch (GameActionException e){}
		
	}
	
	// MARK: Building
	
	public Boolean canBuild(Direction direction, RobotType type) throws GameActionException {
		
		if (this.distanceTo(this.HQLocation()) > MAX_BUILD_TILES) return false;
		if (type.oreCost > this.broadcaster.budgetForType(type)) return false;
		if (!this.robotController.hasBuildRequirements(type)) return false;
		if (!this.robotController.canBuild(direction, type)) return false;
		return true;
		
	}
	
	public Boolean tryBuild(Direction direction, RobotType type) throws GameActionException {
		
		if (this.canBuild(direction, type)) {
			
			this.build(direction, type);
			return true;
			
		}
		return false;
		
	}
	
	public void build(Direction direction, RobotType type) throws GameActionException {
		
		this.robotController.build(direction, type);
		this.broadcaster.decrementBudget(type, type.oreCost);
		this.broadcaster.incrementBuildingRobotCountFor(type);
		this.broadcaster.incrementSpentOre(type.oreCost);
		
	}
	
	// MARK: Directions
	
	public Direction randomDirection() {
		
		int rand = this.random.nextInt(directions.length);
		return directions[rand];
		
	}
	
	public int directionToInt(Direction d) {
		switch(d) {
			case NORTH:
				return 0;
			case NORTH_EAST:
				return 1;
			case EAST:
				return 2;
			case SOUTH_EAST:
				return 3;
			case SOUTH:
				return 4;
			case SOUTH_WEST:
				return 5;
			case WEST:
				return 6;
			case NORTH_WEST:
				return 7;
			default:
				return -1;
		}
	}
	
	// MARK: Distance
	
	public int distanceTo(MapLocation location) {
		
		return this.robotController.getLocation().distanceSquaredTo(location);
		
	}
	
	// MARK: Locations
	
	public MapLocation[] towerLocations() {
		
		return this.robotController.senseTowerLocations();
		
	}
	
	public MapLocation HQLocation() {
		
		return this.robotController.senseHQLocation();
		
	}
	
	// MARK: Locations (Enemy)

	public MapLocation[] enemyTowerLocations() {
		
		return this.robotController.senseEnemyTowerLocations();
		
	}
	
	public MapLocation bestObjective() {
		
		MapLocation bestLocation = null;
		int closestTowerDistance = Integer.MAX_VALUE;
		
		MapLocation[] towers = this.enemyTowerLocations();
		for (MapLocation tower : towers) {
			
			int distance = this.enemyHQLocation().distanceSquaredTo(tower);
			if (distance < closestTowerDistance) {
				
				bestLocation = tower;
				closestTowerDistance = distance;
				
			}
			
		}
		
		if (bestLocation == null) {
			
			bestLocation = this.enemyHQLocation();
			
		}
		
		return bestLocation;
		
	}
	
	public MapLocation enemyHQLocation() {
		
		return this.robotController.senseEnemyHQLocation();
		
	}
	
	// MARK: Mining
	
	public boolean tryMine(Boolean mineClose) throws GameActionException {
		
		if (!this.robotController.isCoreReady()) return false;
		if (this.type != RobotType.BEAVER && this.type != RobotType.MINER) return false;
		if (this.robotController.senseOre(this.robotController.getLocation()) > 0) return false;
		if (this.distanceTo(this.HQLocation()) <= 2 && !mineClose) return false;
		if (!this.robotController.canMine()) return false;
		
		this.robotController.mine();
		
		return true;
		
	}
	
	// MARK: Movement
	
	public Boolean shouldMove() {
		
		return this.stopTurns == 0;
		
	}
	
	public void moveToward(MapLocation location) throws GameActionException {
		
		if (location == null) return; 
		if (!this.shouldMove()) return;
		
		Direction direction = this.robotController.getLocation().directionTo(location);
		int directionInteger = this.directionToInt(direction);
		
		int[] offsets = {0,1,-1,2,-2};
		for (int offset : offsets) {
			
			direction = directions[(directionInteger + offset + 8) % 8];
			if (this.moveTo(direction)) return;
			
		}
		
	}
	
	public Boolean moveTo(Direction direction) throws GameActionException {
		
		if (!this.shouldMove()) return false;
		
		if (this.robotController.canMove(direction)) {
			
			this.robotController.move(direction);
			return true;
			
		}
		return false;
		
	}
	
	public void stopFor(int turns) {
		
		this.stopTurns = turns;
		
	}
	
	// MARK: Nearby
	
	public RobotInfo[] nearbyAllies() throws GameActionException {
		
		return this.robotController.senseNearbyRobots(GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED, this.team);
		
	}
	
	// MARK: Playstyles
	
	public Playstyle currentPlaystyle() throws GameActionException {
		
		Playstyle playstyle = null;
		int playstyleIdentifier = this.broadcaster.currentPlaystyle();
		if (playstyleIdentifier == AggressivePlaystyle.identifierS()) playstyle = new AggressivePlaystyle();
		
		if (playstyle != null) {
			
			if (this.playstyle != null) {
				
				if (playstyle.identifierI() == this.playstyle.identifierI()) return this.playstyle;
				
			}
			
			playstyle.broadcaster = this.broadcaster;
			this.playstyle = playstyle;
			return playstyle;
			
		} else {
			
			return null;
			
		}
		
	}
	
	// MARK: Randomness
	
	public Boolean should(int probability) {
		
		probability = Math.max(0, probability);
		probability = Math.min(100, probability);
		int random = this.random.nextInt(100);
		return (random < probability);
		
	}
	
	// MARK: Spawning
	
	public Boolean canSpawn(Direction direction, RobotType type) throws GameActionException {
		
		if (type.oreCost > this.broadcaster.budgetForType(type)) return false;
		if (!this.robotController.hasSpawnRequirements(type)) return false;
		if (!this.robotController.canSpawn(direction, type)) return false;
		return true;
		
	}
	
	public Boolean trySpawn(Direction direction, RobotType type) throws GameActionException {
		
		if (this.canSpawn(direction, type)) {
			
			this.spawn(direction, type);
			return true;
			
		}
		return false;
		
	}
	
	public void spawn(Direction direction, RobotType type) throws GameActionException {
		
		this.robotController.spawn(direction, type);
		this.broadcaster.decrementBudget(type, type.oreCost);
		this.broadcaster.incrementBuildingRobotCountFor(type);
		this.broadcaster.incrementSpentOre(type.oreCost);
		
	}
	
	// MARK: Supply
	
	public void transferSupplyIfPossible() throws GameActionException {
		
		RobotInfo[] allies = this.nearbyAllies();
		double lowestSupply = this.robotController.getSupplyLevel();
		double transferAmount = 0;
		
		MapLocation targetLocation = null;
		for (RobotInfo ally : allies) {
			
			if (ally.supplyLevel < lowestSupply) {
				
				lowestSupply = ally.supplyLevel;
				transferAmount = (this.robotController.getSupplyLevel() - ally.supplyLevel) / 2;
				targetLocation = ally.location;
				
			}
			
		}
		
		if (targetLocation != null) {
			
			this.robotController.transferSupplies((int)transferAmount, targetLocation);
			
		}
		
	}
	
	// MARK: Static Helpers

	public static RobotType type() {
		return RobotType.HQ;
	}

}
