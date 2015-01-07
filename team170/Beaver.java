package team170;

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
					
					int barracks = this.broadcaster.robotCountFor(Barracks.type());
					int minerFactories = this.broadcaster.robotCountFor(MinerFactory.type());
										
					int barracksProbability = (5 - barracks) * 10 + 50;
					if (barracks >= 5) barracksProbability = 0;
					
					int minerFactoryProbability = (2 - minerFactories) * 25 + 50;
					if (minerFactories >= 2) minerFactoryProbability = 0;
					
					if (barracksProbability > 0 || minerFactoryProbability > 0) {
						
						RobotType[] robotTypes = new RobotType[2];
						int[] probabilities = new int[2];
						
						if (barracksProbability > minerFactoryProbability) {
							robotTypes[0] = Barracks.type();
							robotTypes[1] = MinerFactory.type();
							probabilities[0] = barracksProbability;
							probabilities[1] = minerFactoryProbability;
						} else {
							robotTypes[1] = Barracks.type();
							robotTypes[0] = MinerFactory.type();
							probabilities[1] = barracksProbability;
							probabilities[0] = minerFactoryProbability;
						}
						
						for (int i = 0; i < probabilities.length; i++) {
							
							int probability = probabilities[i];
							if (this.should(probability)) {

								RobotType type = robotTypes[i];
								this.tryBuild(this.randomDirection(), type);
								break;
								
							}
							
						}
						
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

	public RobotType getType() {
		return type();
	}
		
	public static RobotType type() {
		return RobotType.BEAVER;
	}

}
