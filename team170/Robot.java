package team170;

import battlecode.common.*;
import java.util.Random;

import team170.locations.LocationController;
import team170.movement.MovementController;
import team170.playstyles.*;
import team170.supply.SupplyController;

public abstract class Robot {
	
	private static final int MAX_BUILD_TILES = 10000;

	// MARK: Instance Variables
	
	// controllers
	public LocationController locationController;
	public MovementController movementController;
	public RobotBroadcaster broadcaster;
	public RobotController robotController;
	public Random random;
	public SupplyController supplyController;
	
	// helpers
	public Team team;
	public RobotType type;
	
	// playstyles
	private Playstyle playstyle;
	
	// MARK: Main Methods
	
	public Robot(RobotController robotController) {
		
		this.broadcaster = new RobotBroadcaster();
		this.locationController = new LocationController();
		this.movementController = new MovementController();
		this.robotController = robotController;
		this.random = new Random(robotController.getID());
		this.supplyController = new SupplyController();
		
		// update the controllers
		
		this.broadcaster.robotController = this.robotController;
		this.locationController.robotController = this.robotController;
		this.locationController.random = this.random;
		this.movementController.robot = this;
		this.supplyController.robot = this;
		
		// setup the helpers
		
		this.team = this.robotController.getTeam();
		this.type = this.robotController.getType();
		
	}
	
	public void run() {

		try {
			this.broadcaster.incrementLivingTalliedRobotCountFor(this.type);
			this.movementController.decrementMoveTurns();
		}
		catch (GameActionException e){}
		
	}
	
	// MARK: Building
	
	public Boolean canBuild(Direction direction, RobotType type) throws GameActionException {
		
		if (this.locationController.distanceTo(this.locationController.HQLocation()) > MAX_BUILD_TILES) return false;
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
	
	// MARK: Mining
	
	public boolean tryMine(Boolean mineClose) throws GameActionException {

		if (this.type != RobotType.BEAVER && this.type != RobotType.MINER) return false;
		
		if (!this.robotController.isCoreReady()) return false;
		if (this.robotController.senseOre(this.robotController.getLocation()) == 0) return false;
		if (this.locationController.distanceTo(this.locationController.HQLocation()) <= 2 && !mineClose) return false;
		if (!this.robotController.canMine()) return false;
		
		this.robotController.mine();
		
		return true;
		
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
		if (playstyleIdentifier == MarineRushPlaystyle.identifierS()) playstyle = new MarineRushPlaystyle();
		
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
		this.broadcaster.incrementSpentOre(type.oreCost);
				
	}
	
	// MARK: Static Helpers

	public static RobotType type() {
		return RobotType.HQ;
	}

}
