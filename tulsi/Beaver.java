package tulsi;

import battlecode.common.*;

public class Beaver extends BattleRobot {

	public Beaver(RobotController robotController) {
		
		super(robotController);

		
		
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
					
					if (this.broadcaster.readBroadcast(69) == 0) {
						
						if (this.robotController.getTeamOre() > Barracks.cost()) {
							
							if (this.robotController.hasBuildRequirements(Barracks.type())) {
								
								Direction direction = this.randomDirection();
								if (this.robotController.canBuild(direction, Barracks.type())) {
									
									this.robotController.build(direction, Barracks.type());
									this.broadcaster.broadcast(69, 1);
									
								}
								
							}
							
						}
						
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
