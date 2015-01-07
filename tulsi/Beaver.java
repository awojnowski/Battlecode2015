package tulsi;

import battlecode.common.*;

public class Beaver extends BattleRobot {

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.canBeMobilized = false;
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			if (this.robotController.isCoreReady()) {
				
				int random = this.random.nextInt(2);
				if (random < 1) {
					
					if (this.robotController.canMine()) {
						
						this.robotController.mine();
						
					}
					
				} else {

					this.moveTo(this.randomDirection());
					
					if (this.currentPlaystyle().shouldSpawnBarracks()) {
						
						this.tryBuild(this.randomDirection(), Barracks.type());
						
					}
					
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
