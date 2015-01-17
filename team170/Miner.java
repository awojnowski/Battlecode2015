package team170;

import team170.movement.*;
import battlecode.common.*;

public class Miner extends BattleRobot {
	
	public Direction facing;
	private int miningTurns;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
		this.facing = this.movementController.randomDirection();
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				// check to see if this nigga under attack
								
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
										
					if (this.miningTurns > 5) {
						
						this.move();
						
					} else {
						
						if (this.tryMine(true)) {
							
							this.miningTurns ++;
							
						} else {

							this.move();
							
						}
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible(false);
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Movement
	
	public void move() throws GameActionException {
		
		if (!this.moveToBestOre(this.facing)) {

			while (!this.movementController.canMoveSafely(this.facing, true, true, true)) {
					
				this.facing = this.movementController.randomDirection();
					
			}
			this.movementController.moveTo(facing);
			
		}
		this.miningTurns = 0;
		
	}
	
    public Boolean moveToBestOre(Direction initialDirection) throws GameActionException {
               
        MapLocation robotLocation = this.robotController.getLocation();
        MapLocation bestOreLocation = null;
        
        double mostOre = 0.0;
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
       
        if (mostOre > 0) {
               
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
