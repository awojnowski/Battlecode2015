package boyer01181424.playstyles;

import battlecode.common.*;
import boyer01181424.*;

public class LauncherPlaystyle extends Playstyle {
	
	public LauncherPlaystyle() {
		
		this.barracksSpawnOrder =     new int[] {};
		this.minerFactorySpawnOrder = new int[] {2, 4, 5};
		this.tankFactorySpawnOrder  = new int[] {};
		this.helipadSpawnOrder =      new int[] {1};
		this.aerospaceLabSpawnOrder = new int[] {3, 6, 7, 8, 9, 10, 11, 12, 13};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder = new int[] {};
		
		this.civicRatios =       new double[] { 0.60, 0.50, 0.50, 0.30, 0.30, 0.30, 0.30, 0.30, 0.25, 0.25, 0.25, 0.25, 0.25, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.02, 0.02, 0.04, 0.06, 0.06, 0.08, 0.08, 0.10, 0.10, 0.10, 0.10, 0.10 };
		
		this.beaverRatios =      new double[] { 0.53, 0.50, 0.20, 0.05, 0.05, 0.05, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.00, 0.40, 0.30, 0.30, 0.50, 0.55, 0.40, 0.30, 0.30, 0.05, 0.05, 0.05, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.droneRatios =       new double[] { 0.47, 0.50, 0.30, 0.20, 0.00, 0.00, 0.00, 0.00, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.10, 0.45, 0.65, 0.45, 0.40, 0.60, 0.60, 0.60, 0.85, 0.85, 0.85, 0.85 };
		this.commanderRatios =   new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
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
		if (totalLaunchers > 8) {
			
			return true;
			
		} else {
			
			if ((clockNumber > 1400 && clockNumber < 2000)) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 3; }
	public static int identifierS() { return 3; }
	
}
