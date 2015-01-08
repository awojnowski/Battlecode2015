package team170.playstyles;

import battlecode.common.*;
import team170.*;

public abstract class Playstyle {
	
	public RobotBroadcaster broadcaster;
	
	public int[] barracksSpawnOrder;
	public int[] minerFactorySpawnOrder;
	public int[] tankFactorySpawnOrder;
	public int[] helipadSpawnOrder;
	public int[] aerospaceLabSpawnOrder;
		
	public Playstyle() {
				
		this.barracksSpawnOrder = new int[] {};
		this.minerFactorySpawnOrder = new int[] {};
		this.tankFactorySpawnOrder = new int[] {};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgeting(int oreMined) throws GameActionException {
		
		;
		
	}
	
	// MARK: Buildings
	
	public RobotType nextBuildingType() throws GameActionException {
		
		RobotType type = null;
		int lowestNumber = Integer.MAX_VALUE;
		
		int totalBarracks = this.broadcaster.robotCountFor(Barracks.type());
		if (this.isBuildOrderLowest(totalBarracks, lowestNumber, this.barracksSpawnOrder)) {
			
			lowestNumber = this.barracksSpawnOrder[totalBarracks];
			type = Barracks.type();
			
		}

		int totalMinerFactories = this.broadcaster.robotCountFor(MinerFactory.type());
		if (this.isBuildOrderLowest(totalMinerFactories, lowestNumber, this.minerFactorySpawnOrder)) {
			
			lowestNumber = this.minerFactorySpawnOrder[totalMinerFactories];
			type = MinerFactory.type();
			
		}

		int totalTankFactories = this.broadcaster.robotCountFor(TankFactory.type());
		if (this.isBuildOrderLowest(totalTankFactories, lowestNumber, this.tankFactorySpawnOrder)) {
			
			lowestNumber = this.tankFactorySpawnOrder[totalTankFactories];
			type = TankFactory.type();
			
		}

		int totalHelipads = this.broadcaster.robotCountFor(Helipad.type());
		if (this.isBuildOrderLowest(totalHelipads, lowestNumber, this.helipadSpawnOrder)) {
			
			lowestNumber = this.helipadSpawnOrder[totalHelipads];
			type = Helipad.type();
			
		}

		int totalAerospaceLabs = this.broadcaster.robotCountFor(AerospaceLab.type());
		if (this.isBuildOrderLowest(totalAerospaceLabs, lowestNumber, this.aerospaceLabSpawnOrder)) {
			
			lowestNumber = this.aerospaceLabSpawnOrder[totalAerospaceLabs];
			type = AerospaceLab.type();
			
		}
				
		return type;
		
	}
	
	private Boolean isBuildOrderLowest(int totalUnits, int lowestNumber, int[] buildOrder) {
		
		if (totalUnits >= buildOrder.length) return false;
		int number = buildOrder[totalUnits];
		if (number < lowestNumber) return true;
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return -1; }
	public static int identifierS() { return -1; }

}
