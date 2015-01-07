package tulsi.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	// MARK: Building Spawning
	
	public Boolean shouldSpawnBarracks() { return this.shouldSpawn(0.8); }
	
	// MARK: Unit Spawning
	
	public Boolean shouldSpawnBeaver() { return this.shouldSpawn(0.35); }
	public Boolean shouldSpawnSoldier() { return this.shouldSpawn(0.9); }
	
	// MARK: Static Helpers

	public static int identifier() {
		
		return 2;
		
	}
	
}
