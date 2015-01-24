package team170;

import battlecode.common.*;
import team170.playstyles.*;
import team170.queue.BuildingQueue;

public class HQ extends BattleRobot {
		
	private int previousOreTotal;
	private int currentOreTotal;
	
	private int oreMined;
	private int oreMinedTurns;
	private static final int ORE_MINED_HOLD_TURNS = 4; // tally ore for this many turns before budgeting it
	
	public HQ(RobotController robotController) {
		
		super(robotController);
		
		this.canBeMobilized = false;
		this.oreMinedTurns = ORE_MINED_HOLD_TURNS;

		try {
			//this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
			//this.broadcaster.setCurrentPlaystyle(LauncherPlaystyle.identifierS());
			this.broadcaster.setCurrentPlaystyle(FinalPlaystyle.identifierS());
			
		} catch (GameActionException e) {}
		
	}

	public void run() {
		
		boolean updatedBudgeting = false;
		try {
			
			updatedBudgeting = this.processNewTurn();
									
		}
		catch (GameActionException e) {}
		
		super.run();
		
		try {
			
			String indicatorString = "";
			indicatorString += "Turn: " + this.currentPlaystyle().buildOrderProgress() + " ";
			indicatorString += "Budgets: ";
			indicatorString += "Civic = " + this.broadcaster.civicBudget() + " ";
			indicatorString += "Supply Depot = " + this.broadcaster.budgetForType(SupplyDepot.type()) + " ";
			indicatorString += "Miner Factory = " + this.broadcaster.budgetForType(MinerFactory.type()) + " ";
			indicatorString += "Tank Factory = " + this.broadcaster.budgetForType(TankFactory.type()) + " ";
			this.robotController.setIndicatorString(1, indicatorString);
			
			indicatorString = "";
			indicatorString += "Beaver = " + this.broadcaster.budgetForType(Beaver.type()) + " ";
			indicatorString += "Miner = " + this.broadcaster.budgetForType(Miner.type()) + " ";
			indicatorString += "Commander = " + this.broadcaster.budgetForType(Commander.type()) + " ";
			indicatorString += "Soldier = " + this.broadcaster.budgetForType(Soldier.type()) + " ";
			indicatorString += "Basher = " + this.broadcaster.budgetForType(Basher.type()) + " ";
			indicatorString += "Tank = " + this.broadcaster.budgetForType(Tank.type()) + " ";
			indicatorString += "Launcher = " + this.broadcaster.budgetForType(Launcher.type()) + " ";
			indicatorString += "Drone = " + this.broadcaster.budgetForType(Drone.type()) + " ";
			this.robotController.setIndicatorString(2, indicatorString);
			
			if (!updatedBudgeting) {
				
				this.processAttacking();
				this.processSpawning();
				this.processSupplyTransfer();
				
			}
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Progress Methods
	
	// @return boolean denotes whether we have updated the budgeting
	private boolean processNewTurn() throws GameActionException {
		
		this.broadcaster.newTurn();
		
		// update the new ore totals
		
		this.previousOreTotal = this.currentOreTotal;
		this.currentOreTotal = (int)this.robotController.getTeamOre();
		
		int currentOre = this.currentOreTotal;
		currentOre += this.broadcaster.oreSpentLastTurn();
		
		int oreMined = currentOre - this.previousOreTotal;
		
		this.oreMined += oreMined;
		this.oreMinedTurns += 1;
		
		// update the budgets if necessary
		if (this.oreMinedTurns > ORE_MINED_HOLD_TURNS) {
			
			this.currentPlaystyle().updateBudgeting(this.oreMinedTurns, this.oreMined);
			
			this.oreMined = 0;
			this.oreMinedTurns = 0;
			return true;
			
		}
		return false;
		
	}
	
	private void processAttacking() throws GameActionException {
		
		if (this.attack()) {
			
			// check if we can deal damage with splash damage
			
			int towers = this.locationController.towerLocations().length; 
			if (towers >= 5) {
									
				MapLocation currentLocation = this.locationController.currentLocation();
				RobotInfo[] enemies = this.unitController.nearbyEnemies(HQ.type().sensorRadiusSquared + 50);
				for (RobotInfo enemy : enemies) {
					
					int distance = currentLocation.distanceSquaredTo(enemy.location);
					if (distance <= 64) {
						
						Direction direction = enemy.location.directionTo(currentLocation);
						MapLocation attackLocation = enemy.location.add(direction);
						
						if (this.robotController.canAttackLocation(attackLocation)) {
							
							this.robotController.attackLocation(attackLocation);
							break;
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	private void processSpawning() throws GameActionException {
		
		if (this.robotController.isCoreReady()) {
			
			if (this.broadcaster.robotCountFor(Beaver.type()) < 15) {
				
				this.trySpawn(Beaver.type());
				
			}
			
		}
		
	}
	
	private void processSupplyTransfer() throws GameActionException {

		this.supplyController.transferSupplyIfPossible(false);
		
	}
	
	// MARK: Static Helpers
	
	public static int friendlyAttackRadiusSquared(int towers) {
		
        int attackRadius = HQ.type().attackRadiusSquared;
        if (towers >= 2) attackRadius = 35;
        return attackRadius;
		
	}
	
	public static int enemyAttackRadiusSquared(int towers) {

        int attackRadius = HQ.type().attackRadiusSquared;
        if (towers >= 2) attackRadius = 35;
        if (towers >= 5) attackRadius = 64;
        return attackRadius;
		
	}
	
	public static RobotType type() {
		return RobotType.HQ;
	}

}
