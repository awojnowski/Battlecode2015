package team170;

import battlecode.common.*;

public class Miner extends BattleRobot {
	
	public Direction facing;
	private int mineTime;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {

				Boolean shouldMine = (this.mineTime < 6);
				if (shouldMine && this.tryMine(false)) {
					
					this.mineTime ++;
					this.robotController.setIndicatorString(1, "MT: " + this.mineTime);
					
				} else { // no ore underneath
					
					while (!this.robotController.canMove(this.facing)) {
						
						this.facing = this.randomDirection();
						
					}
					this.movementController.moveTo(facing);
					this.mineTime = 0;
					this.robotController.setIndicatorString(1, "MT: " + this.mineTime);
					
				}
				
			}
			
			this.transferSupplyIfPossible();
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINER;
	}

}
