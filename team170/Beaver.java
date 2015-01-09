package team170;

import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	public Direction facing;
	public int mineTime;
	
	public Boolean isDesignatedBuilder;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.attackStyle = BattleRobotAttackStyle.STRAFE_ON_ATTACK;
		this.canBeMobilized = false;
		this.facing = this.randomDirection();
		
		try {
			
			if (this.broadcaster.robotCountFor(this.type) < 5 ||
				this.random.nextInt(10) == 4) {
					
				this.isDesignatedBuilder = true;
				this.robotController.setIndicatorString(1, "Designated Builder Bot");
					
			} else {
				
				this.isDesignatedBuilder = false;
				
			}
			
		}
		catch (GameActionException exception) {}
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.isDesignatedBuilder) {
					
					RobotType buildType = this.currentPlaystyle().nextBuildingType();
					if (buildType != null) {
						
						this.tryBuild(this.randomDirection(), buildType);
						
					}
					
				}
				
				Boolean shouldMine = (this.mineTime < 6);
				if (shouldMine && this.tryMine(false)) {
					
					this.mineTime ++;
					this.robotController.setIndicatorString(2, "MT: " + this.mineTime);
					
				} else { // no ore underneath
					
					if (this.isDesignatedBuilder) {

						MapLocation hqLocation = this.locationController.HQLocation();
						if (this.distanceTo(hqLocation) > 64) {

							this.movementController.moveToward(hqLocation);
							
						} else {
							
							this.movementController.moveTo(this.randomDirection());
							
						}
						
					} else {
						
					while (!this.robotController.canMove(this.facing)) {
						
						this.facing = this.randomDirection();
						
					}
					this.movementController.moveTo(facing);
					this.mineTime = 0;
					this.robotController.setIndicatorString(2, "MT: " + this.mineTime);
						
					}
					
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
