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
				
				if (this.robotController.senseOre(this.robotController.getLocation()) > 0) { // on ore
					
					if (this.robotController.canMine()) {
						
						this.robotController.mine();
						
					}
					
				} else { // no ore underneath
					
					while (!this.robotController.canMove(facing)) {
						
						this.facing = this.randomDirection();
						
					}
					this.moveTo(facing);
					
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
