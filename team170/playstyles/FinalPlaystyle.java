package team170.playstyles;

import battlecode.common.*;
import team170.*;

public class FinalPlaystyle extends Playstyle {
	
	public FinalPlaystyle() {
		
		this.barracksSpawnOrder =            new int[] {4};
		this.minerFactorySpawnOrder =        new int[] {1, 5};
		this.tankFactorySpawnOrder  =        new int[] {6, 7, 8, 9, 10, 11, 12};
		this.helipadSpawnOrder =             new int[] {};
		this.aerospaceLabSpawnOrder =        new int[] {};
		this.technologyInstituteSpawnOrder = new int[] {2};
		this.trainingFieldSpawnOrder =       new int[] {3};
		
		this.civicRatios =       new double[] { 0.80, 0.45, 0.50, 0.60, 0.70, 0.40, 0.40, 0.25, 0.25, 0.25, 0.30, 0.30, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.02, 0.02, 0.04, 0.06, 0.08, 0.08, 0.08, 0.08, 0.10, 0.10, 0.10 };
		
		this.beaverRatios =      new double[] { 1.00, 0.20, 0.15, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.80, 0.60, 0.80, 0.80, 0.60, 0.60, 0.50, 0.40, 0.30, 0.20, 0.20, 0.10 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.20, 0.40, 0.40, 0.50, 0.60, 0.70, 0.80, 0.80, 0.90 };
		this.droneRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.commanderRatios =   new double[] { 0.00, 0.00, 0.25, 0.15, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
	}
	
	// MARK: Attacking
	
	public Boolean areDronesRestrictedToMiners() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		if (clockNumber > 800) return true;
		return false;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) throws GameActionException {
				
		int totalTanks = this.broadcaster.robotCountFor(Tank.type());
		if (this.broadcaster.hasSeenLaunchers()) {
			
			if (totalTanks > 20) return true;
			
		} else {
			
			if (clockNumber > 1700 && clockNumber < 2000) {
				
				return true;
				
			} else {
				
				if (totalTanks > 20) return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean shouldBlitzkrieg() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		return clockNumber > 1700 || this.broadcaster.hasSeenLaunchers();
		
	}
	
	public Boolean shouldGoAllOut() {

		int clockNumber = Clock.getRoundNum();
		return clockNumber > 1850;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 4; }
	public static int identifierS() { return 4; }

}
