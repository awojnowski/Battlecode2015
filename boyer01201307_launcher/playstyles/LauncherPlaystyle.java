package boyer01201307_launcher.playstyles;

import battlecode.common.*;
import boyer01201307_launcher.*;

public class LauncherPlaystyle extends Playstyle {
	
	public LauncherPlaystyle() {
		
		this.barracksSpawnOrder =            new int[] {};
		this.minerFactorySpawnOrder =        new int[] {1, 4};
		this.tankFactorySpawnOrder  =        new int[] {};
		this.helipadSpawnOrder =             new int[] {2};
		this.aerospaceLabSpawnOrder =        new int[] {3, 5, 7, 8, 9, 10};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder =       new int[] {};
		
		this.civicRatios =       new double[] { 0.80, 0.45, 0.70, 0.30, 0.30, 0.25, 0.25, 0.10, 0.10, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.04, 0.04, 0.06, 0.08, 0.08, 0.08, 0.08, 0.10 };
		
		this.beaverRatios =      new double[] { 1.00, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.80, 0.70, 0.60, 0.60, 0.50, 0.40, 0.20, 0.20, 0.10 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.droneRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.30, 0.40, 0.40, 0.50, 0.60, 0.80, 0.80, 0.90 };
		this.commanderRatios =   new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
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
			
			if (totalLaunchers > 8) return true;
			
		} else {
			
			if (clockNumber > 1700 && clockNumber < 2000) {
				
				return true;
				
			} else {
				
				if (totalLaunchers > 8) return true;
				
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

	public int identifierI() { return 3; }
	public static int identifierS() { return 3; }
	
}
