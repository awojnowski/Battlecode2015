package team170.playstyles;

import team170.Beaver;
import team170.Miner;
import battlecode.common.GameActionException;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {4};
		this.minerFactorySpawnOrder = new int[] {1, 2, 3};
		this.tankFactorySpawnOrder = new int[] {5, 6, 7, 8, 9, 10};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgeting(int oreMined) throws GameActionException {
		
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
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
