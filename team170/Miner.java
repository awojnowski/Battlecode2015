package team170;

import team170.broadcaster.Broadcaster;
import battlecode.common.*;

public class Miner extends BattleRobot {
	
	private MapLocation desiredOreLocation = null;
	private double desiredOreCurrent = 0;
	private double desiredOreTotal = 0;
	
	public Miner(RobotController robotController) {
		
		super(robotController);
		
		if (this.desiredOreLocation == null) {
			
			try {

				OreLocationResult result = this.bestOreLocation(100);
				if (result != null) {
					
					this.desiredOreLocation = result.location;
					this.desiredOreTotal = result.ore;
					
				}
				
			} catch (GameActionException e) {
			}
			
		}
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			// try find some good ore nearby
			MapLocation currentLocation = this.robotController.getLocation();
			if (this.desiredOreLocation == null) {
				
				this.scanForBetterOreLocation(50, currentLocation);
				
			}

			this.robotController.setIndicatorString(1, "Current ore: " + this.robotController.senseOre(currentLocation));
			
			// run some checks on our desired ore location
			if (this.desiredOreLocation != null) {
				
				this.desiredOreCurrent = this.robotController.senseOre(this.desiredOreLocation);
				
				if (currentLocation.distanceSquaredTo(this.desiredOreLocation) <= this.type.sensorRadiusSquared) {
					
					RobotInfo robot = this.robotController.senseRobotAtLocation(this.desiredOreLocation) ;
					if (robot != null && robot.ID != this.robotController.getID()) {
						
						this.desiredOreLocation = null;
						this.desiredOreCurrent = 0;
						this.desiredOreTotal = 0;
						
					}
					
				}
				
			}

			if (this.desiredOreLocation != null) {
				
				this.robotController.setIndicatorLine(currentLocation, this.desiredOreLocation, 255, 255, 0);
				
			} else {
				
				this.robotController.setIndicatorString(2, "");
				
			}
			
			if (this.robotController.isWeaponReady()) {
				
				this.attack();
				
			}
			
			if (this.robotController.isCoreReady()) {
				
				if (this.currentPlaystyle().shouldGoAllOut(this.robotController.getRoundLimit())) {
					
					this.doAttackerThings();
					
				} else {

					this.doMinerThings();
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	public Boolean canMoveInMilitaryRange() throws GameActionException {
		
		return this.currentPlaystyle().shouldGoAllOut(this.robotController.getRoundLimit());
		
	}
	
	// MARK: Styles
	
	private void doMinerThings() throws GameActionException {
		
		RobotInfo enemy = this.unitController.closestMilitaryAttackerWithinRange();
		if (enemy != null) {
			
			if (this.movementController.fleeFrom(enemy.location)) return;
			
		}

		if (this.desiredOreLocation != null) {

			MapLocation currentLocation = this.robotController.getLocation();
			if (currentLocation.equals(this.desiredOreLocation)) {
				
				final double minimumOre = this.desiredOreTotal * 0.2; 
				if (this.robotController.senseOre(this.desiredOreLocation) < minimumOre || !this.tryMine()) {
					
					this.desiredOreLocation = null;
					this.desiredOreCurrent = 0;
					this.desiredOreTotal = 0;
					
				}
				
			} else {
				
				this.scanForBetterOreLocation(9, currentLocation);
				this.movementController.moveToward(this.desiredOreLocation);
				
			}
			
		}
		
	}
	
	private void doAttackerThings() throws GameActionException {
		
		this.mobilize();
		
	}
	
	// MARK: Mining
	
	private void scanForBetterOreLocation(int radius, MapLocation location) throws GameActionException {
		
		// check to see if we can go to a better ore location
		
		OreDensityResult densityResult = this.currentBestOreDensity();
		OreLocationResult oreLocationResult = this.bestOreLocation(radius);
		if (oreLocationResult != null) {
			
			if (oreLocationResult.ore > this.desiredOreTotal ||
				(oreLocationResult.ore == this.desiredOreTotal && location.distanceSquaredTo(oreLocationResult.location) < location.distanceSquaredTo(this.desiredOreLocation))) {

				this.desiredOreLocation = oreLocationResult.location;
				this.desiredOreCurrent = oreLocationResult.ore;
				this.desiredOreTotal = this.desiredOreCurrent;
				
				if (densityResult != null && densityResult.density - this.desiredOreTotal > 4 && densityResult.density > this.desiredOreTotal * 1.5) {
					
					this.desiredOreLocation = densityResult.location;
					this.desiredOreCurrent = densityResult.density;
					this.desiredOreTotal = this.desiredOreCurrent;
					
				}
				
			}
			
		} else {
			
			if (densityResult != null) {
				
				this.desiredOreLocation = densityResult.location;
				this.desiredOreTotal = densityResult.density;
				
			}
			
		}
		
	}
	
	private class OreLocationResult {
		MapLocation location;
		double ore;
		double density;
	}
	
	private OreLocationResult bestOreLocation(int radius) throws GameActionException {
		
		double bestOre = 0;
		int squares = 0;
		double totalOre = 0;
		MapLocation bestLocation = null;

		MapLocation currentLocation = this.robotController.getLocation();
		MapLocation[] towerLocations = this.locationController.enemyTowerLocations();
		MapLocation[] mapLocations = MapLocation.getAllMapLocationsWithinRadiusSq(currentLocation, radius);
		for (MapLocation location : mapLocations) {
			
			TerrainTile tile = this.robotController.senseTerrainTile(location);
			if (tile != TerrainTile.NORMAL) continue;
			
			// check to see if its ore is good
			
			double ore = this.robotController.senseOre(location);
			totalOre += ore;
			squares ++;
			
			if (ore > bestOre) {

				if (!this.isOreLocationValid(location, towerLocations)) continue;
				bestOre = ore;
				bestLocation = location;
				
			} else if (ore == bestOre && bestLocation != null) {
				
				// check to see if this new ore location is closer
				if (currentLocation.distanceSquaredTo(location) < currentLocation.distanceSquaredTo(bestLocation)) {

					if (!this.isOreLocationValid(location, towerLocations)) continue;
					bestLocation = location;
					
				}
				
			}
			
		}
		
		OreLocationResult result = null;
		if (bestLocation != null) {
			
			result = new OreLocationResult();
			result.location = bestLocation;
			result.ore = bestOre;
			result.density = totalOre / squares;
			
			this.broadcastOreDensity(result.density, result.location);
			this.robotController.setIndicatorString(1, "Ore density: " + result.density);
			
		}
		return result;
		
	}
	
	private boolean isOreLocationValid(MapLocation location, MapLocation[] towerLocations) {
		
		// check to make sure it's not in enemy tower range or HQ range
		
		boolean inTowerRange = false;
		for (MapLocation towerLocation : towerLocations) {
			
			if (location.distanceSquaredTo(towerLocation) <= Tower.type().attackRadiusSquared) {
				
				inTowerRange = true;
				continue;
				
			}
			
		}
		if (location.distanceSquaredTo(this.locationController.enemyHQLocation()) <= HQ.enemyAttackRadiusSquared(towerLocations.length)) {
			
			inTowerRange = true;
			
		}
		if (inTowerRange) return false;
		
		// check to make sure another unit isn't on the square
		
		boolean onRobot = false;
		try {
			
			RobotInfo robot = this.robotController.senseRobotAtLocation(location);
			if (robot != null) onRobot = true;
			
		}
		catch (GameActionException exception) {	
			
		}
		if (onRobot) return false;
		
		return true;
		
	}
	
	// MARK: Ore Broadcast

	private static int ORE_BROADCAST_CHANNEL = 18739;
	
	private static class OreDensityResult {
		int turn;
		double density;
		MapLocation location;
		
		public void write(Broadcaster broadcaster) throws GameActionException {
			
			broadcaster.broadcast(ORE_BROADCAST_CHANNEL + 0, (int)(this.density * 10000));
			broadcaster.broadcast(ORE_BROADCAST_CHANNEL + 1, this.turn);
			broadcaster.broadcast(ORE_BROADCAST_CHANNEL + 2, this.location.x);
			broadcaster.broadcast(ORE_BROADCAST_CHANNEL + 3, this.location.y);
			
		}
		
		public static OreDensityResult currentWrittenDensityResult(Broadcaster broadcaster) throws GameActionException {
			
			OreDensityResult result = new OreDensityResult();
			double d = broadcaster.readBroadcast(ORE_BROADCAST_CHANNEL + 0) / 10000.0;
			int t    = broadcaster.readBroadcast(ORE_BROADCAST_CHANNEL + 1);
			int x    = broadcaster.readBroadcast(ORE_BROADCAST_CHANNEL + 2);
			int y    = broadcaster.readBroadcast(ORE_BROADCAST_CHANNEL + 3);
			if (d == 0) return null;
			
			result.density = d;
			result.turn = t;
			result.location = new MapLocation(x, y);
			return result;
			
		}
	}
	
	private OreDensityResult currentBestOreDensity() throws GameActionException {
		
		return OreDensityResult.currentWrittenDensityResult(this.broadcaster);
		
	}
	
	private void broadcastOreDensity(double density, MapLocation location) throws GameActionException {
		
		OreDensityResult result = new OreDensityResult();
		result.turn = Clock.getRoundNum();
		result.density = density;
		result.location = location;
		
		OreDensityResult existingResult = new OreDensityResult();
		
		final int expirationTurns = 30; // amount of turns that a better result will expire in
		if (existingResult.density > density && existingResult.turn > result.turn - expirationTurns) return;
			
		result.write(this.broadcaster);
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINER;
	}

}
