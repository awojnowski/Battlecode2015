package team170.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder = new int[] {1, 4, 5, 8, 9};
		this.minerFactorySpawnOrder = new int[] {2, 3};
		this.tankFactorySpawnOrder = new int[] {6, 7, 10, 11};
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
