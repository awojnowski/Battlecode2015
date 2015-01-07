package tulsi;

import battlecode.common.*;

public class MinerFactory extends Robot {

	public MinerFactory(RobotController robotController) {
		
		super(robotController);
		
		
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			if (this.robotController.isCoreReady()) {
				
				if (this.currentPlaystyle().shouldSpawnMiner()) {
					
					this.trySpawn(this.randomDirection(), Miner.type());
					
				}
								
			}
							
		} catch (GameActionException exception) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.MINERFACTORY;
	}
	
	public static int identifierInteger() {
		return 8;
	}

}