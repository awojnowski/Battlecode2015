package team170.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {4};
		this.minerFactorySpawnOrder = new int[] {1, 2, 3};
		this.tankFactorySpawnOrder = new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		//                                   1     2     3     4     5     6     7     8     9     10    11     12     13     14    End  
		this.civicRatios =   new double[] { 0.50, 0.35, 0.35, 0.35, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25,  0.25,  0.25,  0.25,  0.0 };
		// CIVIC RATIO IS % OF MONEY ALLOCATED TO ECON, RATIOS BELOW ARE AMOUNT OF UNIT TYPE TO ALL UNITS
		this.beaverRatios =  new double[] { 1.00, 0.70, 0.40, 0.10, 0.10, 0.10, 0.05, 0.05, 0.05, 0.05, 0.025, 0.025, 0.025, 0.025, 0.025 };
		this.minerRatios =   new double[] { 0.00, 0.30, 0.60, 0.80, 0.80, 0.50, 0.30, 0.30, 0.30, 0.30, 0.20,  0.20,  0.20,  0.20,  0.20 };
		this.soldierRatios = new double[] { 0.00, 0.00, 0.00, 0.10, 0.10, 0.20, 0.10, 0.10, 0.10, 0.10, 0.10,  0.10,  0.10,  0.10,  0.10 };
		this.tankRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.20, 0.55, 0.55, 0.55, 0.55, 0.675, 0.675, 0.675, 0.675, 0.675 };
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
