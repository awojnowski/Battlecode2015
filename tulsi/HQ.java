package tulsi;

import battlecode.common.*;
import tulsi.playstyles.*;

public class HQ extends BattleRobot {

	public HQ(RobotController robotController) {
		
		super(robotController);
		
		this.canBeMobilized = false;

		try {
			this.broadcaster.setCurrentPlaystyle(TurtlePlaystyle.identifier());
		} catch (GameActionException e) {}
		
	}

	public void run() {
		
		super.run();
		
		try {
			
			attack();
			
			if (this.currentPlaystyle().shouldSpawnBeaver()) {
				
				this.trySpawn(this.randomDirection(), Beaver.type());
				
			}
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int cost() {
		return 0;
	}
		
	public static RobotType type() {
		return RobotType.HQ;
	}

}
