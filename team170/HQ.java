package team170;

import battlecode.common.*;
import team170.playstyles.*;

public class HQ extends BattleRobot {
	
	private int previousOreTotal;
	private int currentOreTotal;

	public HQ(RobotController robotController) {
		
		super(robotController);
		
		this.canBeMobilized = false;

		try {
			this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
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
			
			// update the budgets
			
			int remainingOre = oreMined;
			
			int budget = (int)(remainingOre * 0.35);
			this.broadcaster.incrementCivicBudget(budget);
			remainingOre -= budget;
						
			int totalBeavers = this.broadcaster.robotCountFor(Beaver.type());
			if (totalBeavers < 10) {
				
				budget = (int)(remainingOre * ((10 - totalBeavers) / 10.0));
				this.broadcaster.incrementBudget(Beaver.type(), budget);
				remainingOre -= budget;
								
			}
			
			this.broadcaster.incrementBudget(Miner.type(), remainingOre);
			
		}
		catch (GameActionException e) {}
		
		super.run();
		
		try {
			
			attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Beaver.type()) < 15) {
					
					this.trySpawn(this.randomDirection(), Beaver.type());
					
				}
				
			}
			
			this.transferSupplyIfPossible();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.HQ;
	}

}
