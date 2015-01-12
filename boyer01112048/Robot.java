package boyer01112048;

import battlecode.common.*;
import java.util.Random;
import boyer01112048.locations.*;
import boyer01112048.movement.*;
import boyer01112048.playstyles.*;
import boyer01112048.supply.*;
import boyer01112048.units.*;

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
	public UnitController unitController;
	
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
		this.unitController = new UnitController();
		
		// update the controllers
		
		this.broadcaster.robotController = this.robotController;
		this.locationController.robotController = this.robotController;
		this.movementController.robot = this;
		this.supplyController.robot = this;
		this.unitController.robot = this;
		
		// setup the helpers
		
		this.team = this.robotController.getTeam();
		this.type = this.robotController.getType();
		
	}
	
	public void run() {

		try {
			this.broadcaster.incrementLivingTalliedRobotCountFor(this.type);
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
		this.broadcaster.beginBuildingRobot(type);
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
		this.broadcaster.incrementSpentOre(type.oreCost);
				
	}
	
	// MARK: Static Helpers

	public static RobotType type() {
		return RobotType.HQ;
	}

}
