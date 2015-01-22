package boyer01201307_launcher;

import battlecode.common.*;

import java.util.Random;
import boyer01201307_launcher.broadcaster.*;
import boyer01201307_launcher.locations.*;
import boyer01201307_launcher.movement.*;
import boyer01201307_launcher.playstyles.*;
import boyer01201307_launcher.supply.*;
import boyer01201307_launcher.units.*;

public abstract class Robot {
	
	// MARK: Instance Variables
	
	// controllers
	public LocationController locationController;
	public MovementController movementController;
	public Broadcaster broadcaster;
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
		
		this.broadcaster = new Broadcaster();
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
	
	// MARK: Attacking
	
	public Boolean canAttackInTowerRange() throws GameActionException {

		int clockNumber = Clock.getRoundNum();
		if ((clockNumber > 1700 && clockNumber < 2000)) {
			
			return true;
			
		} else {
			
			int militaryEnemies = this.unitController.nearbyMilitaryEnemies().length;
			int militaryAllies = this.unitController.nearbyMilitaryAllies(150).length; // 150 to see around the tower
			if (militaryAllies > militaryEnemies * 1.2 && militaryAllies > 7) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean canAttackInHQRange(int totalTowers) throws GameActionException {
		
		return totalTowers == 0;
		
	}
	
	// MARK: Mining
	
	public boolean tryMine() throws GameActionException {

		if (this.type != RobotType.BEAVER && this.type != RobotType.MINER) return false;
		
		if (!this.robotController.isCoreReady()) return false;
		if (this.robotController.senseOre(this.robotController.getLocation()) == 0) return false;
		if (!this.robotController.canMine()) return false;
		
		this.robotController.mine();
		
		return true;
		
	}
	
	// MARK: Playstyles
	
	public Playstyle currentPlaystyle() throws GameActionException {
		
		Playstyle playstyle = null;
		int playstyleIdentifier = this.broadcaster.currentPlaystyle();
		if (playstyleIdentifier == AggressivePlaystyle.identifierS()) playstyle = new AggressivePlaystyle();
		if (playstyleIdentifier == LauncherPlaystyle.identifierS()) playstyle = new LauncherPlaystyle();
		
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
	
	public Boolean trySpawn(RobotType type) throws GameActionException {
		
		for (int i = 0; i < 8; i ++) {
			
			Direction direction = MovementController.directionFromInt(i);
			if (this.canSpawn(direction, type)) {
				
				this.spawn(direction, type);
				return true;
				
			}
			
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
