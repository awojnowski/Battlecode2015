package team170.playstyles;

import battlecode.common.*;
import team170.*;

public class FinalPlaystyle extends Playstyle {
	
	public FinalPlaystyle() {
		
		this.barracksSpawnOrder =            new int[] {};
		this.minerFactorySpawnOrder =        new int[] {1};
		this.tankFactorySpawnOrder  =        new int[] {};
		this.helipadSpawnOrder =             new int[] {2};
		this.aerospaceLabSpawnOrder =        new int[] {3};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder =       new int[] {};
		
		this.civicRatios =       new double[] { 0.00, 0.00, 0.00, 0.00 };
		
	}
	
	// @param turns    the amount of turns that the budget was saved up for
	// @param progress the progress that we are in the build order
	public void updateBudgetingForBuildOrderProgress(int turns, int oreMined, int progress) throws GameActionException {
		
		int remainingOre = oreMined;
		
		// check if we need to take some for the supply depots
		if (progress >= this.civicRatios.length - 1) {
			
			if (this.broadcaster.budgetForType(RobotType.SUPPLYDEPOT) < 250) {
				
				this.broadcaster.incrementBudget(RobotType.SUPPLYDEPOT, (int)(remainingOre * 0.1));
				remainingOre *= 0.9;
				
			}
			
		}
		
		// firstly check the beavers
		final int beavers = this.broadcaster.robotCountFor(Beaver.type());
		int beaverOreAllocation = 0;
		if (beavers == 0) {
			
			beaverOreAllocation = 100;
			
		}
		
		// miners
		final int minerFactories = this.broadcaster.robotCountFor(MinerFactory.type());
		int minerOreAllocation = 0;
		if (minerFactories > 0 && this.broadcaster.robotCountFor(Miner.type()) < 30) {

			minerOreAllocation = (60 / (20 / turns));
			if (this.broadcaster.budgetForType(Miner.type()) >= minerFactories * 60) minerOreAllocation = 0;
			
		}

		// launchers
		final int aerospaceLabs = this.broadcaster.robotCountFor(AerospaceLab.type());
		int launcherOreAllocation = aerospaceLabs * (400 / (100 / turns));
		if (this.broadcaster.budgetForType(Launcher.type()) >= aerospaceLabs * 400) launcherOreAllocation = 0;
		
		// drones
		int droneOreAllocation = 0;
		if (Clock.getRoundNum() > 600 && this.broadcaster.robotCountFor(Drone.type()) == 0) {
			
			droneOreAllocation = 8;
			
		}
		
		int total = beaverOreAllocation + minerOreAllocation + launcherOreAllocation + droneOreAllocation;
		double multiplier = (total > remainingOre) ? remainingOre / (float)total : 1.0;

		this.broadcaster.incrementBudget(Beaver.type(), (int)(beaverOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Miner.type(), (int)(minerOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Launcher.type(), (int)(launcherOreAllocation * multiplier));
		this.broadcaster.incrementBudget(Drone.type(), (int)(droneOreAllocation * multiplier));
		
		remainingOre -= total * multiplier;
		if (remainingOre > 40 && oreMined < 500 /* first turn */) {
			
			beaverOreAllocation = 10;
			this.broadcaster.incrementBudget(Beaver.type(), beaverOreAllocation);
			remainingOre -= beaverOreAllocation;
			
		}
		this.broadcaster.incrementCivicBudget(remainingOre);
		
	}
	
	// MARK: Attacking
	
	public Boolean areDronesRestrictedToMiners() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		if (clockNumber > 800) return true;
		return false;
		
	}
	
	// Buildings
	
	public RobotType nextBuildOrderExpansionBuilding() {
		
		return AerospaceLab.type();
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber, int roundLimit) throws GameActionException {
		
		int totalLaunchers = this.broadcaster.robotCountFor(Launcher.type());
		if (this.broadcaster.hasSeenLaunchers()) {
			
			if (totalLaunchers > 6) return true;
			
		} else {
			
			if (clockNumber > roundLimit - 300) {
				
				return true;
				
			} else {
				
				if (totalLaunchers > 6) return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean shouldBlitzkrieg(int roundLimit) throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		return clockNumber > roundLimit - 300;
		
	}
	
	public Boolean shouldGoAllOut(int roundLimit) {

		int clockNumber = Clock.getRoundNum();
		return clockNumber > roundLimit - 150;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 4; }
	public static int identifierS() { return 4; }

}
