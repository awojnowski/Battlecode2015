package team170;

import battlecode.common.*;

public class Beaver extends BattleRobot {
	
	public Direction facing;
	
	public Boolean isDesignatedBuilder;

	public Beaver(RobotController robotController) {
		
		super(robotController);

		this.attackStyle = BattleRobotAttackStyle.STRAFE_ON_ATTACK;
		this.canBeMobilized = false;
		this.facing = this.locationController.randomDirection();
		this.isDesignatedBuilder = true;
		
	}

	public void run() {
		
		super.run();
				
		try {
			
			this.attack();
			
			if (this.robotController.isCoreReady()) {
				
				if (this.isDesignatedBuilder) {
					
					Boolean builtBuilding = false;
					
					RobotType buildType = this.currentPlaystyle().nextBuildingType();
					if (buildType != null) {
						
						builtBuilding = this.tryBuild(this.locationController.randomDirection(), buildType);
						
					}
					
					// try to build a supply depot
					if (!builtBuilding) {
						
						this.tryBuild(this.locationController.randomDirection(), SupplyDepot.type());
						
					}
					
				}
				
				if (this.tryMine(false)) {
					

					
				} else { // no ore underneath
					
					if (this.isDesignatedBuilder) {

						MapLocation hqLocation = this.locationController.HQLocation();
						if (this.locationController.distanceTo(hqLocation) > 64) {

							this.movementController.moveToward(hqLocation);
							
						} else {
							
							this.movementController.moveTo(this.locationController.randomDirection());
							
						}
						
					} else {
						
					while (!this.robotController.canMove(this.facing)) {
						
						this.facing = this.locationController.randomDirection();
						
					}
					this.movementController.moveTo(facing);
						
					}
					
				}
				
			}
			
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
