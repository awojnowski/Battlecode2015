package team170.playstyles;

import team170.*;
import battlecode.common.*;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {4};
		this.minerFactorySpawnOrder = new int[] {1, 2, 3};
		this.tankFactorySpawnOrder = new int[] {5, 6, 7, 8, 9, 10};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		//                                   1     2     3     4     5     6     7     8     9     10    End   
		this.civicRatios =   new double[] { 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35 };
		this.beaverRatios =  new double[] {  };
		this.minerRatios =   new double[] {  };
		this.soldierRatios = new double[] {  };
		this.tankRatios =    new double[] {  };
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgetingForBuildOrderProgress(int oreMined, int progress) throws GameActionException {

		int remainingOre = oreMined;
		
		double civicRatio = this.civicRatios[progress];
		
		int totalSpawned = this.broadcaster.totalSpawnedRobotCount();
		double beaverRatio = this.civicRatios[progress];
		double minerRatio = this.civicRatios[progress];
		double soldierRatio = this.civicRatios[progress];
		double tankRatio = this.civicRatios[progress];
		
		int budget = (int)(remainingOre * civicRatio);
		this.broadcaster.incrementCivicBudget(budget);
		remainingOre -= budget;
		
		// now update the remaining ore
		
		this.broadcaster.incrementBudget(Beaver.type(), remainingOre);
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
