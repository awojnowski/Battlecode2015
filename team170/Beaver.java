package team170;

import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	public Direction facing;
	public int mineTime;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.attackStyle = BattleRobotAttackStyle.STRAFE_ON_ATTACK;
		this.canBeMobilized = false;
		this.facing = this.randomDirection();
				
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				RobotType buildType = this.currentPlaystyle().nextBuildingType();
				if (buildType != null) {
					
					this.tryBuild(this.randomDirection(), buildType);
					
				}
				
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
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
