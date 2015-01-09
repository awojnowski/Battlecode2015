package team170.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {4};
		this.minerFactorySpawnOrder = new int[] {1, 2, 3};
		this.tankFactorySpawnOrder = new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		this.civicRatios =       new double[] { 0.50, 0.65, 0.75, 0.85, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.00 };
		this.supplyDepotRatios = new double[] { 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05 };
		
		this.beaverRatios =      new double[] { 1.00, 0.10, 0.10, 0.10, 0.10, 0.10, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.90, 0.90, 0.50, 0.40, 0.30, 0.15, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.40, 0.50, 0.30, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.30, 0.55, 0.55, 0.55, 0.85, 0.85, 0.85, 0.85, 0.85, 0.85 };
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) {
		
		if ((clockNumber > 1700 && clockNumber < 2000)) {
			
			return true;
			
		}
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
