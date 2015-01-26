package boyer01261512.playstyles;

import battlecode.common.*;
import boyer01261512.*;
import boyer01261512.broadcaster.*;

public abstract class Playstyle {
	
	public Broadcaster broadcaster;
	
	public int[] barracksSpawnOrder;
	public int[] minerFactorySpawnOrder;
	public int[] tankFactorySpawnOrder;
	public int[] helipadSpawnOrder;
	public int[] aerospaceLabSpawnOrder;
	public int[] technologyInstituteSpawnOrder;
	public int[] trainingFieldSpawnOrder;
	
	public double[] civicRatios;
	public double[] supplyDepotRatios;
	
	public double[] beaverRatios;
	public double[] minerRatios;
	public double[] soldierRatios;
	public double[] tankRatios;
	public double[] droneRatios;
	public double[] launcherRatios;
	public double[] commanderRatios;
		
	public Playstyle() {
		
		this.barracksSpawnOrder = new int[] {};
		this.minerFactorySpawnOrder = new int[] {};
		this.tankFactorySpawnOrder = new int[] {};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder = new int[] {};
		
		// NOTE: ratios should go from [0, n] where n is the number of build order buildings
		
		this.civicRatios = new double[] {};
		this.supplyDepotRatios = new double[] {};
		
		this.beaverRatios = new double[] {};
		this.minerRatios = new double[] {};
		this.soldierRatios = new double[] {};
		this.tankRatios = new double[] {};
		this.droneRatios = new double[] {};
		this.launcherRatios = new double[] {};
		this.commanderRatios = new double[] {};
		
	}
	
	// MARK: Attacking
	
	public Boolean areDronesRestrictedToMiners() throws GameActionException {
		
		return true;
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgeting(int turns, int oreMined) throws GameActionException {
		
		int buildOrderProgress = this.buildOrderProgress();
		if (buildOrderProgress == Integer.MAX_VALUE) buildOrderProgress = this.civicRatios.length;
		buildOrderProgress --;
		
		this.updateBudgetingForBuildOrderProgress(turns, oreMined, buildOrderProgress);		
		
	}
	
	public void updateBudgetingForBuildOrderProgress(int turns, int oreMined, int progress) throws GameActionException {
		
		int remainingOre = oreMined;
		
		// account for the ore taken off the top
		
		int civicOre = (int)(oreMined * this.civicRatios[progress]);
		this.broadcaster.incrementCivicBudget(civicOre);
		remainingOre -= civicOre;
		
		int supplyOre = 0;
		if (this.broadcaster.budgetForType(SupplyDepot.type()) < 250) {
			
			supplyOre = (int)(oreMined * this.supplyDepotRatios[progress]);
			this.broadcaster.incrementBudget(SupplyDepot.type(), supplyOre);
			remainingOre -= supplyOre;
			
		}
		
		this.processTopLevelBudgeting(progress, oreMined, remainingOre);
		
		// calculate unit ratios
		int oreUsed = 0;
		
		int oreAllocation = Math.min((int)(remainingOre * this.beaverRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Beaver.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.minerRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Miner.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.soldierRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Soldier.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.tankRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Tank.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.droneRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Drone.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.launcherRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Launcher.type(), oreAllocation);
		oreUsed += oreAllocation;
		
		oreAllocation = Math.min((int)(remainingOre * this.commanderRatios[progress]), (remainingOre - oreUsed));
		this.broadcaster.incrementBudget(Commander.type(), oreAllocation);
		oreUsed += oreAllocation;

		this.broadcaster.incrementCivicBudget(Math.max(0, remainingOre - oreUsed));
       
	}
	
	public int processTopLevelBudgeting(int progress, int oreMined, int remainingOre) throws GameActionException {
		
		int supplyOre;
		
		// make sure we have minimum beavers at all times
		if (this.broadcaster.robotCountFor(Beaver.type()) < 3 && this.beaverRatios[progress] == 0.0) {

			supplyOre = (int)(oreMined * 0.20);
			this.broadcaster.incrementBudget(Beaver.type(), supplyOre);
			remainingOre -= supplyOre;
			
		}
		
		// some game checks
		if (progress > 3) {
			
			// do we need a computer?
			if (this.broadcaster.robotCountFor(TechnologyInstitute.type()) > 0 && this.broadcaster.robotCountFor(Computer.type()) == 0) {
			
				supplyOre = Math.min(remainingOre, 10);
				this.broadcaster.incrementBudget(Computer.type(), supplyOre);
				remainingOre -= supplyOre;
				System.out.println("Adding " + supplyOre);
				
			}

			// check if we need to build another commander B)
			if (this.broadcaster.robotCountFor(Commander.type()) == 0) {
			
				supplyOre = (int)(oreMined * 0.05);
				this.broadcaster.incrementBudget(Commander.type(), supplyOre);
				remainingOre -= supplyOre;
				
			}

			if (progress > 8) {
							
				// make sure we have minimum miners at all times
				if (this.broadcaster.robotCountFor(Miner.type()) < 20) {

					supplyOre = (int)(oreMined * 0.05);
					this.broadcaster.incrementBudget(Miner.type(), supplyOre);
					remainingOre -= supplyOre;
					
				}
				
				if (this.broadcaster.robotCountFor(Barracks.type()) > 0 && this.broadcaster.hasSeenLaunchers()) {

					supplyOre = (int)(oreMined * 0.05);
					this.broadcaster.incrementBudget(Basher.type(), supplyOre);
					remainingOre -= supplyOre;
					
				}
								
			}
			
		}
		return remainingOre;
		
	}
	
	// MARK: Buildings
	
	public RobotType nextBuildingType() throws GameActionException {
		
		int progress = this.buildOrderProgress();
		
		if (progress == Integer.MAX_VALUE) {
			
			// we are the end so we can check if we need to build any buildings or whatnot
			final int tankFactoryOreCost = TankFactory.type().oreCost;
			if (this.broadcaster.budgetForType(Tank.type()) > 1500 && this.broadcaster.budgetForType(TankFactory.type()) < tankFactoryOreCost) {
				
				this.broadcaster.decrementBudget(Tank.type(), tankFactoryOreCost);
				this.broadcaster.incrementBudget(TankFactory.type(), tankFactoryOreCost);
				return TankFactory.type();
				
			} else if (this.broadcaster.budgetForType(TankFactory.type()) > tankFactoryOreCost) {
				
				return TankFactory.type();
				
			}
			
			final int aerospaceLabOreCost = AerospaceLab.type().oreCost;
			if (this.broadcaster.budgetForType(Launcher.type()) > 1500 && this.broadcaster.budgetForType(AerospaceLab.type()) < aerospaceLabOreCost) {
				
				this.broadcaster.decrementBudget(Launcher.type(), aerospaceLabOreCost);
				this.broadcaster.incrementBudget(AerospaceLab.type(), aerospaceLabOreCost);
				return AerospaceLab.type();
				
			} else if (this.broadcaster.budgetForType(AerospaceLab.type()) > aerospaceLabOreCost) {
				
				return AerospaceLab.type();
				
			}
			
			return null;
			
		}
				
		for (int value : this.barracksSpawnOrder) if (value == progress) return Barracks.type();
		for (int value : this.minerFactorySpawnOrder) if (value == progress) return MinerFactory.type();
		for (int value : this.tankFactorySpawnOrder) if (value == progress) return TankFactory.type();
		for (int value : this.helipadSpawnOrder) if (value == progress) return Helipad.type();
		for (int value : this.aerospaceLabSpawnOrder) if (value == progress) return AerospaceLab.type();
		for (int value : this.technologyInstituteSpawnOrder) if (value == progress) return TechnologyInstitute.type();
		for (int value : this.trainingFieldSpawnOrder) if (value == progress) return TrainingField.type();
		
		return null;
		
	}
	
	// if the build order is done and we still have money, what should be built?
	public RobotType nextBuildOrderExpansionBuilding() {
		
		return null;
		
	}
	
	public int buildOrderProgress() throws GameActionException {
		
		int lowestNumber = Integer.MAX_VALUE;
		
		int total = this.broadcaster.robotCountFor(Barracks.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.barracksSpawnOrder)) {
			lowestNumber = this.barracksSpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(MinerFactory.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.minerFactorySpawnOrder)) {
			lowestNumber = this.minerFactorySpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(TankFactory.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.tankFactorySpawnOrder)) {
			lowestNumber = this.tankFactorySpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(Helipad.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.helipadSpawnOrder)) {
			lowestNumber = this.helipadSpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(AerospaceLab.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.aerospaceLabSpawnOrder)) {
			lowestNumber = this.aerospaceLabSpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(TechnologyInstitute.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.technologyInstituteSpawnOrder)) {
			lowestNumber = this.technologyInstituteSpawnOrder[total];
		}

		total = this.broadcaster.robotCountFor(TrainingField.type());
		if (this.isBuildOrderLowest(total, lowestNumber, this.trainingFieldSpawnOrder)) {
			lowestNumber = this.trainingFieldSpawnOrder[total];
		}
				
		return lowestNumber;
		
	}
	
	private Boolean isBuildOrderLowest(int totalUnits, int lowestNumber, int[] buildOrder) {
		
		if (totalUnits >= buildOrder.length) return false;
		int number = buildOrder[totalUnits];
		if (number < lowestNumber) return true;
		return false;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber, int roundLimit) throws GameActionException {
		
		return false;
		
	}
	
	public Boolean shouldBlitzkrieg(int roundLimit) throws GameActionException {
		
		return false;
		
	}
	
	public Boolean shouldGoAllOut(int roundLimit) {
		
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return -1; }
	public static int identifierS() { return -1; }

}
