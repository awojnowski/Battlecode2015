package tulsi.playstyles;

public class TurtlePlaystyle extends Playstyle {
	
	// MARK: Building Spawning
	
	public Boolean shouldSpawnBarracks() { return this.shouldSpawn(0.3); }
	
	// MARK: Unit Spawning
	
	public Boolean shouldSpawnBeaver() { return this.shouldSpawn(0.5); }
	public Boolean shouldSpawnSoldier() { return this.shouldSpawn(0.8); }
	
	// MARK: Static Helpers

	public static int identifier() {
		
		return 1;
		
	}

}
