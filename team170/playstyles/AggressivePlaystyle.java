package team170.playstyles;

import battlecode.common.*;
import team170.*;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		/*this.barracksSpawnOrder =     new int[] {3};
		this.minerFactorySpawnOrder = new int[] {2, 5, 6};
		this.tankFactorySpawnOrder  = new int[] {4, 7, 8, 9, 10, 11, 12, 13, 14};
		this.helipadSpawnOrder =      new int[] {1};
		this.aerospaceLabSpawnOrder = new int[] {};
		this.technologyInstituteSpawnOrder = new int[] {};
		this.trainingFieldSpawnOrder = new int[] {};
		
		this.civicRatios =       new double[] { 0.60, 0.50, 0.50, 0.40, 0.65, 0.65, 0.40, 0.40, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.02, 0.02, 0.04, 0.06, 0.06, 0.08, 0.08, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 };
		
		this.beaverRatios =      new double[] { 0.53, 0.50, 0.20, 0.05, 0.05, 0.05, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.00, 0.40, 0.45, 0.65, 0.65, 0.55, 0.40, 0.20, 0.20, 0.05, 0.05, 0.05, 0.05, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.10, 0.30, 0.30, 0.30, 0.40, 0.60, 0.70, 0.70, 0.75, 0.75, 0.75, 0.75, 0.75 };
		this.droneRatios =       new double[] { 0.47, 0.50, 0.30, 0.20, 0.00, 0.00, 0.00, 0.00, 0.10, 0.10, 0.20, 0.20, 0.20, 0.20, 0.20 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };*/
		
		this.barracksSpawnOrder =            new int[] {2};
		this.minerFactorySpawnOrder =        new int[] {1, 3, 5};
		this.tankFactorySpawnOrder  =        new int[] {4, 6, 7, 10, 11, 12, 13};
		this.helipadSpawnOrder =             new int[] {};
		this.aerospaceLabSpawnOrder =        new int[] {};
		this.technologyInstituteSpawnOrder = new int[] {8};
		this.trainingFieldSpawnOrder =       new int[] {9};
		
		this.civicRatios =       new double[] { 0.80, 0.50, 0.50, 0.40, 0.65, 0.65, 0.40, 0.40, 0.25, 0.25, 0.25, 0.25, 0.10, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.02, 0.02, 0.04, 0.06, 0.06, 0.08, 0.08, 0.08, 0.08, 0.10, 0.10, 0.10 };
		
		this.beaverRatios =      new double[] { 1.00, 0.40, 0.10, 0.05, 0.05, 0.05, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.40, 0.60, 0.50, 0.65, 0.65, 0.55, 0.50, 0.30, 0.30, 0.30, 0.20, 0.05, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.20, 0.30, 0.15, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.30, 0.30, 0.30, 0.40, 0.50, 0.70, 0.70, 0.70, 0.80, 0.95, 0.95 };
		this.droneRatios =       new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
	}
	
	// MARK: Attacking
	
	public Boolean canAttackInTowerRange() throws GameActionException {

		int totalTanks = this.broadcaster.robotCountFor(Tank.type());
		
		int roundNumber = Clock.getRoundNum();
		int tanksRequired = 50 - (int)Math.round((Math.max(0, roundNumber - 1000) / 500.0) * 20);
		if (totalTanks > tanksRequired) {
			
			return true;
			
		} else {

			if ((roundNumber > 1700 && roundNumber < 2000)) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean canAttackInHQRange(int totalTowers) throws GameActionException {
		
		return totalTowers == 0;
		
	}
	
	public Boolean areDronesRestrictedToMiners() throws GameActionException {
		
		int clockNumber = Clock.getRoundNum();
		if (clockNumber > 800) return true;
		return false;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) throws GameActionException {
		
		int totalTanks = this.broadcaster.robotCountFor(Tank.type());
		if (totalTanks > 50) {
			
			return true;
			
		} else if (totalTanks > 70 && !this.broadcaster.hasSeenLaunchers()) {
			
			return true;
			
		} else {
			
			if ((clockNumber > 1700 && clockNumber < 2000)) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return 2; }
	public static int identifierS() { return 2; }
	
}
