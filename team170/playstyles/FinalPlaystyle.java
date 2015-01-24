package team170.playstyles;

import battlecode.common.*;
import team170.*;
import team170.queue.BuildingQueue;

public class FinalPlaystyle extends Playstyle {
	
	public FinalPlaystyle() {
		
		this.barracksSpawnOrder =            new int[] {2};
		this.minerFactorySpawnOrder =        new int[] {1};
		this.tankFactorySpawnOrder  =        new int[] {};
		this.helipadSpawnOrder =             new int[] {3};
		this.aerospaceLabSpawnOrder =        new int[] {4};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder =       new int[] {};
		
		this.civicRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.00 };
		
		this.minerRatios =         new double[] { 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.soldierRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.67 };
		this.droneRatios =         new double[] { 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =      new double[] { 0.00, 0.00, 0.00, 0.00, 0.33 };
		
	}
	
	// @param turns    the amount of turns that the budget was saved up for
	// @param progress the progress that we are in the build order
	public void updateBudgetingForBuildOrderProgress(int turns, int oreMined, int progress) throws GameActionException {
		
		int remainingOre = oreMined;
		
		// check if we need to take some for the supply depots
		if (progress >= this.civicRatios.length - 1) {
			
			this.broadcaster.incrementBudget(RobotType.SUPPLYDEPOT, (int)(remainingOre * 0.1));
			remainingOre *= 0.9;
			
		}
		
		// firstly check the beavers
		final int beavers = this.broadcaster.robotCountFor(Beaver.type());
		int beaverOreAllocation = 0;
		if (beavers == 0) {
			
			beaverOreAllocation = 100;
			
		} else {
			
			if (this.broadcaster.civicBudget() + this.broadcaster.budgetForType(SupplyDepot.type()) > 400) {
				
				beaverOreAllocation = 5;
				
			}
			
		}
		
		// miners
		int minerOreAllocation = 0;
		if (this.broadcaster.robotCountFor(MinerFactory.type(), false) > 0 && this.broadcaster.robotCountFor(Miner.type()) < 28) {

			minerOreAllocation = (60 / (20 / turns));
			
		}
		
		// soldiers
		final int barracks = this.broadcaster.robotCountFor(Barracks.type(), false);
		int soldierOreAllocation = barracks * (60 / (20 / turns));

		// launchers
		final int aerospaceLabs = this.broadcaster.robotCountFor(AerospaceLab.type(), false);
		int launcherOreAllocation =  aerospaceLabs * (400 / (100 / turns));
		
		int total = beaverOreAllocation + minerOreAllocation + soldierOreAllocation + launcherOreAllocation;
		double multiplier = (total > remainingOre) ? remainingOre / (float)total : 1.0;

		this.broadcaster.incrementBudget(Beaver.type(), (int)(beaverOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Miner.type(), (int)(minerOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Soldier.type(), (int)(soldierOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Launcher.type(), (int)(launcherOreAllocation * multiplier));
		
		remainingOre -= total * multiplier;
		this.broadcaster.incrementCivicBudget(remainingOre);
		
	}
	
	// MARK: Attacking
	
	public Boolean areDronesRestrictedToMiners() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		if (clockNumber > 800) return true;
		return false;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) throws GameActionException {
		
		int totalLaunchers = this.broadcaster.robotCountFor(Launcher.type());
		if (this.broadcaster.hasSeenLaunchers()) {
			
			if (totalLaunchers > 6) return true;
			
		} else {
			
			if (clockNumber > 1700 && clockNumber < 2000) {
				
				return true;
				
			} else {
				
				if (totalLaunchers > 6) return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean shouldBlitzkrieg() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		return clockNumber > 1700;
		
	}
	
	public Boolean shouldGoAllOut() {

		int clockNumber = Clock.getRoundNum();
		return clockNumber > 1850;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 4; }
	public static int identifierS() { return 4; }

}
