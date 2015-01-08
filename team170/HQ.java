package team170;

import battlecode.common.*;
import team170.playstyles.*;

public class HQ extends BattleRobot {

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
			
			int budget = 3;
			budget += (int)Math.max(0, this.robotController.getTeamOre() - this.broadcaster.civicBudget() - 800) / 70; // account for lots of ore
			budget += Math.max(0, 500 - Clock.getRoundNum()) / 100;
			if (this.currentPlaystyle().nextBuildingType() == null) budget = 0;
			this.broadcaster.incrementCivicBudget(budget);
			
			this.robotController.setIndicatorString(1, "Civic budget: " + this.broadcaster.civicBudget());
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
