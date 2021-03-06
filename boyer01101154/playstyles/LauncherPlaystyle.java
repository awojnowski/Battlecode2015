package boyer01101154.playstyles;

import battlecode.common.*;

public class LauncherPlaystyle extends Playstyle {

	public LauncherPlaystyle() {
		
		this.barracksSpawnOrder = new int[] {3, 4, 5, 6, 7, 8, 9, 10};
		this.minerFactorySpawnOrder = new int[] {1, 2};
		this.tankFactorySpawnOrder = new int[] {};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		this.civicRatios =       new double[] { 0.50, 0.65, 0.75, 0.85, 0.25, 0.20, 0.15, 0.10, 0.05, 0.05, 0.00 };
		this.supplyDepotRatios = new double[] { 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05 };
		
		this.beaverRatios =      new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
	}
	
	// MARK: Attacking
	
	public Boolean canAttackInTowerRange() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		if ((clockNumber > 950 && clockNumber < 1200) ||
			(clockNumber > 1600 && clockNumber < 2000)) {
				
			return true;
			
		}
		return false;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) {
		
		if ((clockNumber > 600 && clockNumber < 650) ||
			(clockNumber > 800 && clockNumber < 850) ||
			(clockNumber > 1200 && clockNumber < 1250) ||
			(clockNumber > 1500 && clockNumber < 2000)) {
			
			return true;
			
		}
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 4; }
	public static int identifierS() { return 4; }

}
