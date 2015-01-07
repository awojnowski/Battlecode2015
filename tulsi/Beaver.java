package tulsi;

import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	Direction facing;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.canBeMobilized = false;
		this.facing = this.randomDirection();
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
				
				this.attack();
				
				if (this.robotController.senseOre(this.robotController.getLocation()) > 0) { // on ore
					
					if (this.robotController.canMine()) {
						
						this.robotController.mine();
						
					}
					
				} else { // no ore underneath

					if (this.broadcaster.readBroadcast(69) == 0 && this.distanceTo(this.HQLocation()) < 25) {
						
						if (this.tryBuild(this.randomDirection(), Barracks.type())) {

							this.broadcaster.broadcast(69, 1);
							
						}
						
					}
					
					while (!this.robotController.canMove(facing))
						this.facing = this.randomDirection();
					
					this.moveTo(facing);
					
				}
				
			}
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 100;
	}
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
