package team170;

import battlecode.common.*;
import team170.playstyles.*;

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
			int towerCount = this.locationController.towerLocations().length;
			if (towerCount < 2) {
				System.out.println("Using marine rush playstyle.");
				this.broadcaster.setCurrentPlaystyle(MarineRushPlaystyle.identifierS());
			} else {
				System.out.println("Using aggressive playstyle.");
				this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
			}
		} catch (GameActionException e) {}
		
	}

	public void run() {
		
		try {
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
				
			}
						
		}
		catch (GameActionException e) {}
		
		super.run();
		
		try {
			
			attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Beaver.type()) < 15) {
					
					this.trySpawn(this.locationController.randomDirection(), Beaver.type());
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
			this.robotController.setIndicatorString(1, "Turn: " + this.currentPlaystyle().buildOrderProgress() + " Budgets: Civic: " + this.broadcaster.civicBudget() + " Beavers: " + this.broadcaster.budgetForType(Beaver.type()) + " Miners: " + this.broadcaster.budgetForType(Miner.type()) + " Soldiers: " + this.broadcaster.budgetForType(Soldier.type()) + " Tanks: " + this.broadcaster.budgetForType(Tank.type()));
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.HQ;
	}

}
