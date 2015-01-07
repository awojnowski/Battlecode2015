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
			this.broadcaster.resetRobotCounts();
			
			int budget = 3;
			budget += (int)Math.max(0, this.robotController.getTeamOre() - this.broadcaster.currentCivicBudget() - 800) / 70; // account for lots of ore
			budget += Math.max(0, 500 - Clock.getRoundNum()) / 100;
			this.broadcaster.incrementCurrentCivicBudget(budget);
			
			this.robotController.setIndicatorString(1, "Civic budget: " + this.broadcaster.currentCivicBudget());
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

	public RobotType getType() {
		return type();
	}
		
	public static RobotType type() {
		return RobotType.HQ;
	}

}