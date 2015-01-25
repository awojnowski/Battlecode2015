package team170;

import team170.movement.*;
import battlecode.common.*;

public class Miner extends BattleRobot {
	
	private final int idealThreshold = 15; // because the miner always mines for 5 turns, it will mine at max rate for all 5 turns
	
	public Direction facing;
	private int miningTurns;
	private int miningThreshold = this.idealThreshold;
	private int movementsWithoutMining;
	private double totalOreSeen = 0.0;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
		this.facing = this.movementController.randomDirection();
		
	}

	public void run() {
		
		super.run();
		this.robotController.setIndicatorString(1, "Mining Threshold: " + this.miningThreshold + " Movements Without Mining: " + this.movementsWithoutMining);
				
		try {
			
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
		
		// check to see if this guy under attack
		
		RobotInfo closestEnemy = this.unitController.closestMilitaryAttackerWithinRange();
		if (closestEnemy != null) {
			
			Boolean moved = false;
			if (this.unitController.nearbyAllies().length >= 3 && this.unitController.nearbyEnemies().length < 3) {
				
				moved = this.movementController.moveToward(closestEnemy.location) != null;
				
			} else {
				
				moved = this.movementController.fleeFrom(closestEnemy.location) != null;
				
			}
			if (moved) { 
				
				this.miningTurns = 0;
				this.facing = this.movementController.randomDirection();
				
			}
			
		} else {
			
			// nah he gucci
			
			double currentOre = this.robotController.senseOre(this.locationController.currentLocation());
			
			if (this.miningTurns > 5 || (this.miningTurns == 0 && currentOre < this.miningThreshold)) { // only mine good stuff but always mine for 5 turns
				
				if (this.miningTurns == 0 && currentOre > 0) {
					
					this.movementsWithoutMining ++;
					this.totalOreSeen += currentOre;
					
				}
				
				this.move();
				
			} else {
				
				if (this.tryMine()) {
					
					this.miningTurns ++;
					
					if (currentOre >= this.idealThreshold) { // if the miner finds good ore
						
						this.movementsWithoutMining = 0;
						this.miningThreshold = this.idealThreshold;
						
					}
					
				} else {

					this.move();
					
				}
				
			}
			
		}
		
	}
	
	private void doAttackerThings() throws GameActionException {
		
		this.mobilize();
		
	}
	
	// MARK: Movement
	
	private void move() throws GameActionException {
		
		if (!this.moveToBestOre(this.facing)) {

			while (!this.movementController.canMoveSafely(this.facing, true, true, true)) {
					
				this.facing = this.movementController.randomDirection();
					
			}
			this.movementController.moveTo(facing);
			
		}
			
		this.miningTurns = 0;
		
		if (this.movementsWithoutMining >= 20) { // if the miner has moved 20 times without mining
			
			this.miningThreshold = (int)(this.totalOreSeen / this.movementsWithoutMining);
			this.movementsWithoutMining = 0;
			this.totalOreSeen = 0.0;
			
		}
		
	}
	
    public Boolean moveToBestOre(Direction initialDirection) throws GameActionException {
               
        MapLocation robotLocation = this.robotController.getLocation();
        MapLocation bestOreLocation = null;
        
        double oreThreshold = this.miningThreshold - 0.001;
        
        if (oreThreshold <= 0) // don't want the miners thinking they found a good spot at 0 ore
        	oreThreshold = 0.001;
        
        double mostOre = oreThreshold;
        int safestDirection = MovementController.directionToInt(initialDirection);
        int[] offsets = { 0, -1, 1, -2, 2, -3, 3, 4 };
       
        for (int offset: offsets) {

        	Direction currDirection = MovementController.directionFromInt(safestDirection + offset);
        	MapLocation currLocation = robotLocation.add(currDirection);
        	double currOre = this.robotController.senseOre(currLocation);
           
            if (currOre > mostOre && this.movementController.canMoveSafely(currDirection, true, true, true)) {
                   
                bestOreLocation = currLocation;
                mostOre = currOre;
                   
            }
               
        }
       
        if (mostOre > oreThreshold) {
               
            this.movementController.moveToward(bestOreLocation);
            return true;
               
        }
        return false;
       
    }
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINER;
	}

}
