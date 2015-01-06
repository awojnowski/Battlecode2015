package tulsi;

import battlecode.common.*;

public class Beaver extends Robot {

	public Beaver(RobotController robotController) {
		
		super(robotController);

		
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
				
				int random = this.random.nextInt(2);
				if (random == 0) {
					
					if (this.robotController.canMine()) {
						
						this.robotController.mine();
						
					}
					
				} else {

					this.moveTo(this.randomDirection());
					
				}
				
			}
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}

}
