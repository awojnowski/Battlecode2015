package boyer01111515.playstyles;

import battlecode.common.*;
import boyer01111515.*;

public class AggressivePlaystyle extends Playstyle {
	
	public AggressivePlaystyle() {
		
		this.barracksSpawnOrder =     new int[] {5};
		this.minerFactorySpawnOrder = new int[] {2, 3, 4};
		this.tankFactorySpawnOrder  = new int[] {6, 7, 8, 9, 10, 11, 12, 13, 14};
		this.helipadSpawnOrder =      new int[] {1};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		this.civicRatios =       new double[] { 0.60, 0.25, 0.50, 0.65, 0.65, 0.65, 0.40, 0.40, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08 };
		
		this.beaverRatios =      new double[] { 0.50, 0.80, 0.40, 0.10, 0.05, 0.05, 0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.00, 0.20, 0.60, 0.75, 0.75, 0.65, 0.40, 0.20, 0.20, 0.05, 0.05, 0.05, 0.05, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.20, 0.20, 0.30, 0.60, 0.80, 0.80, 0.95, 0.95, 0.95, 0.95, 0.95 };
		this.droneRatios =       new double[] { 0.50, 0.20, 0.40, 0.30, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		
		/*this.barracksSpawnOrder =     new int[] {};
		this.minerFactorySpawnOrder = new int[] {3, 4, 5};
		this.tankFactorySpawnOrder  = new int[] {};
		this.helipadSpawnOrder =      new int[] {1};
		this.aerospaceLabSpawnOrder = new int[] {2, 6, 7, 8, 9, 10};
		
		this.civicRatios =       new double[] { 0.60, 0.25, 0.50, 0.65, 0.65, 0.65, 0.40, 0.40, 0.25, 0.25, 0.00 };
		this.supplyDepotRatios = new double[] { 0.00, 0.00, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08 };
		
		this.beaverRatios =      new double[] { 0.50, 0.60, 0.40, 0.10, 0.05, 0.05, 0.05, 0.00, 0.00, 0.00, 0.00 };
		this.minerRatios =       new double[] { 0.00, 0.00, 0.30, 0.60, 0.75, 0.75, 0.65, 0.40, 0.20, 0.20, 0.05 };
		this.soldierRatios =     new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.tankRatios =        new double[] { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.droneRatios =       new double[] { 0.50, 0.20, 0.20, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 };
		this.launcherRatios =    new double[] { 0.00, 0.20, 0.10, 0.10, 0.20, 0.20, 0.30, 0.60, 0.80, 0.80, 0.95 };*/
		
	}
	
	// MARK: Attacking
	
	public Boolean canAttackInTowerRange() throws GameActionException {

		int totalTanks = this.broadcaster.robotCountFor(Tank.type());
		if (totalTanks > 50) {
			
			return true;
			
		} else {

			int clockNumber = Clock.getRoundNum();
			if ((clockNumber > 1700 && clockNumber < 2000)) {
				
				return true;
				
			}
			
		}
		return false;
		
	}
	
	public Boolean canAttackInHQRange(int totalTowers) throws GameActionException {
		
		return totalTowers == 0;
		
	}
	
	// MARK: Mobilizing
	
	public Boolean canMobilizeForClockNumber(int clockNumber) throws GameActionException {
		
		int totalTanks = this.broadcaster.robotCountFor(Tank.type());
		if (totalTanks > 10) {
			
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
