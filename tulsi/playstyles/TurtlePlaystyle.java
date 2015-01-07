package tulsi.playstyles;

public class TurtlePlaystyle extends Playstyle {
	
	// MARK: Building Spawning
	
	public Boolean shouldSpawnBarracks() { return this.shouldSpawn(0.3); }
	public Boolean shouldSpawnMinerFactory() { return this.shouldSpawn(0.35); }
	
	// MARK: Unit Spawning
	
	public Boolean shouldSpawnBeaver() { return this.shouldSpawn(0.5); }
	public Boolean shouldSpawnMiner() { return this.shouldSpawn(0.35); }
	public Boolean shouldSpawnSoldier() { return this.shouldSpawn(0.8); }
	
	// MARK: Static Helpers

	public int identifierI() { return 1; }
	public static int identifierS() { return 1; }

}
