package team170.playstyles;

import java.util.Random;

public abstract class Playstyle {
	
	private Random random;
	
	public Playstyle() {
		
		this.random = new Random();
		
	}
	
	// MARK: Building Spawning
	
	public Boolean shouldSpawnBarracks() { return this.shouldSpawn(0.5); }
	public Boolean shouldSpawnMinerFactory() { return this.shouldSpawn(0.5); }
	
	// MARK: Unit Spawning
	
	public Boolean shouldSpawnBeaver() { return this.shouldSpawn(0.5); }
	public Boolean shouldSpawnMiner() { return this.shouldSpawn(0.5); }
	public Boolean shouldSpawnSoldier() { return this.shouldSpawn(0.5); }
	
	// MARK: Randomization
	
	public Boolean shouldSpawn(double probability) {
		
		int size = 1000;
		int random = this.random.nextInt(size);
		return (random <= probability * size);
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return -1; }
	public static int identifierS() { return -1; }

}
