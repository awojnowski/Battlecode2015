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
	
	public double[] civicRatios;
	public double[] beaverRatios;
	public double[] minerRatios;
	public double[] soldierRatios;
	public double[] tankRatios;
		
	public Playstyle() {
				
		this.barracksSpawnOrder = new int[] {};
		this.minerFactorySpawnOrder = new int[] {};
		this.tankFactorySpawnOrder = new int[] {};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		this.civicRatios = new double[] {};
		this.beaverRatios = new double[] {};
		this.minerRatios = new double[] {};
		this.soldierRatios = new double[] {};
		this.tankRatios = new double[] {};
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgeting(int oreMined) throws GameActionException {
		
		int buildOrderProgress = this.buildOrderProgress();
		if (buildOrderProgress == Integer.MAX_VALUE) buildOrderProgress = this.civicRatios.length;
		buildOrderProgress --;
		
		this.updateBudgetingForBuildOrderProgress(oreMined, buildOrderProgress);		
		
	}
	
	public void updateBudgetingForBuildOrderProgress(int oreMined, int progress) throws GameActionException {
		
		; // subclass override this
		
	}
	
	// MARK: Buildings
	
	public RobotType nextBuildingType() throws GameActionException {
		
		int progress = this.buildOrderProgress();
		if (progress == Integer.MAX_VALUE) return null;
				
		for (int value : this.barracksSpawnOrder) if (value == progress) return Barracks.type();
		for (int value : this.minerFactorySpawnOrder) if (value == progress) return MinerFactory.type();
		for (int value : this.tankFactorySpawnOrder) if (value == progress) return TankFactory.type();
		for (int value : this.helipadSpawnOrder) if (value == progress) return Helipad.type();
		for (int value : this.aerospaceLabSpawnOrder) if (value == progress) return AerospaceLab.type();
		
		return null;
		
	}
	
	public int buildOrderProgress() throws GameActionException {
		
		int lowestNumber = Integer.MAX_VALUE;
		
		int totalBarracks = this.broadcaster.robotCountFor(Barracks.type());
		if (this.isBuildOrderLowest(totalBarracks, lowestNumber, this.barracksSpawnOrder)) {
			lowestNumber = this.barracksSpawnOrder[totalBarracks];
		}

		int totalMinerFactories = this.broadcaster.robotCountFor(MinerFactory.type());
		if (this.isBuildOrderLowest(totalMinerFactories, lowestNumber, this.minerFactorySpawnOrder)) {
			lowestNumber = this.minerFactorySpawnOrder[totalMinerFactories];
		}

		int totalTankFactories = this.broadcaster.robotCountFor(TankFactory.type());
		if (this.isBuildOrderLowest(totalTankFactories, lowestNumber, this.tankFactorySpawnOrder)) {
			lowestNumber = this.tankFactorySpawnOrder[totalTankFactories];
		}

		int totalHelipads = this.broadcaster.robotCountFor(Helipad.type());
		if (this.isBuildOrderLowest(totalHelipads, lowestNumber, this.helipadSpawnOrder)) {
			lowestNumber = this.helipadSpawnOrder[totalHelipads];
		}

		int totalAerospaceLabs = this.broadcaster.robotCountFor(AerospaceLab.type());
		if (this.isBuildOrderLowest(totalAerospaceLabs, lowestNumber, this.aerospaceLabSpawnOrder)) {
			lowestNumber = this.aerospaceLabSpawnOrder[totalAerospaceLabs];
		}
				
		return lowestNumber;
		
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
