package tulsi;

import battlecode.common.*;
import tulsi.playstyles.*;

public class HQ extends BattleRobot {

	public HQ(RobotController robotController) {
		
		super(robotController);
		
		this.canBeMobilized = false;

		try {
			this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
		} catch (GameActionException e) {}
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.currentPlaystyle().shouldSpawnBeaver()) {
					
					this.trySpawn(this.randomDirection(), Beaver.type());
					
				}
				
			}
			
			this.transferSupplyIfPossible();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.HQ;
	}
	
	public static int identifierInteger() {
		return 1;
	}

}
