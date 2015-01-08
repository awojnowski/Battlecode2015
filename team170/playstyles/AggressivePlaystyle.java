package team170.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {4};
		this.minerFactorySpawnOrder = new int[] {1, 2, 3};
		this.tankFactorySpawnOrder = new int[] {5, 6, 7, 8, 9, 10};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		//                                   1     2     3     4     5     6     7     8     9     10    End   
		this.civicRatios =   new double[] { 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.00 };
		this.beaverRatios =  new double[] {  };
		this.minerRatios =   new double[] {  };
		this.soldierRatios = new double[] {  };
		this.tankRatios =    new double[] {  };
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
