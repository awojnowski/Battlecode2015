package boyer01101154;

import battlecode.common.*;

public class Miner extends BattleRobot {
	
	public Direction facing;
	private int miningTurns;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
		this.facing = this.locationController.randomDirection();
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				// check to see if this nigga under attack
								
				RobotInfo closestEnemy = this.unitController.closestMilitaryAttackerWithinRange();
				if (closestEnemy != null) {
					
					if (this.movementController.fleeFrom(closestEnemy.location)) {
						
						this.miningTurns = 0;
						this.facing = this.locationController.randomDirection();
						
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
			
			this.supplyController.transferSupplyIfPossible();
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Movement
	
	public void move() throws GameActionException {
		
		if (!this.moveToBestOre(this.facing)) {

			while (!this.movementController.canMoveSafely(this.facing, true)) {
					
				this.facing = this.locationController.randomDirection();
					
			}
			this.movementController.moveTo(facing);
			
		}
		this.miningTurns = 0;
		
	}
	
    public Boolean moveToBestOre(Direction initialDirection) throws GameActionException {
        
        if (!this.movementController.shouldMove()) return false;
       
        MapLocation robotLocation = this.robotController.getLocation();
        MapLocation bestOreLocation = null;
        
        double mostOre = 0.0;
        int safestDirection = this.movementController.directionToInt(initialDirection);
        int[] offsets = { 0, -1, 1, -2, 2, -3, 3, 4 };
       
        for (int offset: offsets) {

        	Direction currDirection = this.locationController.DIRECTIONS[(safestDirection + offset + 8) % 8];
        	MapLocation currLocation = robotLocation.add(currDirection);
        	double currOre = this.robotController.senseOre(currLocation);
           
            if (currOre > mostOre && this.movementController.canMoveSafely(currDirection, true)) {
                   
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
