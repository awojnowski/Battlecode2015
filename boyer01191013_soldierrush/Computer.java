package boyer01191013_soldierrush;

import battlecode.common.*;

public class Computer extends Robot {

	public Computer(RobotController robotController) {
		
		super(robotController);
		
		
		
	}
	
	public void run() {
		
		super.run();
		
		try {
			
			String indicatorString = "";
			indicatorString += "Turn: " + this.currentPlaystyle().buildOrderProgress() + " ";
			indicatorString += "Budgets: ";
			indicatorString += "Civic = " + this.broadcaster.civicBudget() + " ";
			indicatorString += "Supply Depot = " + this.broadcaster.budgetForType(SupplyDepot.type()) + " ";
			indicatorString += "Beaver = " + this.broadcaster.budgetForType(Beaver.type()) + " ";
			indicatorString += "Miner = " + this.broadcaster.budgetForType(Miner.type()) + " ";
			this.robotController.setIndicatorString(1, indicatorString);
			
			indicatorString = "";
			indicatorString += "Commander = " + this.broadcaster.budgetForType(Commander.type()) + " ";
			indicatorString += "Soldier = " + this.broadcaster.budgetForType(Soldier.type()) + " ";
			indicatorString += "Basher = " + this.broadcaster.budgetForType(Basher.type()) + " ";
			indicatorString += "Tank = " + this.broadcaster.budgetForType(Tank.type()) + " ";
			indicatorString += "Launcher = " + this.broadcaster.budgetForType(Launcher.type()) + " ";
			indicatorString += "Drone = " + this.broadcaster.budgetForType(Drone.type()) + " ";
			this.robotController.setIndicatorString(2, indicatorString);
		
		}
		catch (GameActionException exception) {
			
		}
	
	}
	
	// MARK: Static Helpers
	
	public static RobotType type() {
		return RobotType.COMPUTER;
	}

}
