package boyer01161942;

import battlecode.common.*;
import boyer01161942.playstyles.*;

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
			this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
			//this.broadcaster.setCurrentPlaystyle(LauncherPlaystyle.identifierS());
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
			
			this.robotController.setIndicatorString(1, "Budget: Beavers = " + this.broadcaster.budgetForType(Beaver.type()));
			
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
			
			this.currentPlaystyle().updateBudgeting(this.oreMined);
			
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

		this.supplyController.transferSupplyIfPossible();
		
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
