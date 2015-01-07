package team170.playstyles;

public class AggressivePlaystyle extends Playstyle {
	
	// MARK: Building Spawning
	
	public Boolean shouldSpawnBarracks() { return this.shouldSpawn(0.8); }
	public Boolean shouldSpawnMinerFactory() { return this.shouldSpawn(0.5); }
	
	// MARK: Unit Spawning
	
	public Boolean shouldSpawnBeaver() { return this.shouldSpawn(0.35); }
	public Boolean shouldSpawnMiner() { return this.shouldSpawn(0.35); }
	public Boolean shouldSpawnSoldier() { return this.shouldSpawn(0.9); }
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
