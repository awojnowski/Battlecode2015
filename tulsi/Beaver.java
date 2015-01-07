package tulsi;

import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	public Direction facing;

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
				
				if (this.robotController.senseOre(this.robotController.getLocation()) > 0 &&
					this.distanceTo(this.HQLocation()) > 2) { // on ore
					
					if (this.robotController.canMine()) {
						
						this.robotController.mine();
						
					}
					
				} else { // no ore underneath

					Boolean built = false; 
					if (!built && this.currentPlaystyle().shouldSpawnMinerFactory()) {
						
						built = this.tryBuild(this.randomDirection(), MinerFactory.type());
						
					}
					if (!built && this.currentPlaystyle().shouldSpawnBarracks()) {
						
						built = this.tryBuild(this.randomDirection(), Barracks.type());
						
					}
					
					while (!this.robotController.canMove(facing)) {
						
						this.facing = this.randomDirection();
						
					}
					this.moveTo(facing);
					
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
	
	public static int identifierInteger() {
		return 2;
	}

}
