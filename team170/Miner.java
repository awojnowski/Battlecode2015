package team170;

import battlecode.common.*;

public class Miner extends BattleRobot {
	
	public Direction facing;

	public Miner(RobotController robotController) {
		
		super(robotController);
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {

				if (this.tryMine(false)) {
					

					
				} else { // no ore underneath
					
					while (!this.robotController.canMove(this.facing)) {
						
						this.facing = this.randomDirection();
						
					}
					this.movementController.moveTo(facing);
					
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
