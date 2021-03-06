package boyer01161509;

import battlecode.common.*;
import boyer01161509.playstyles.*;

public class HQ extends BattleRobot {
	
	private int previousOreTotal;
	private int currentOreTotal;
	
	private int oreMined;
	private int oreMinedTurns;
	private static final int ORE_MINED_HOLD_TURNS = 4; // tally ore for this many turns before budgeting it
	
	public HQ(RobotController robotController) {
		
		super(robotController);
		
		this.canBeMobilized = false;
		this.oreMinedTurns = ORE_MINED_HOLD_TURNS;

		try {
			this.broadcaster.setCurrentPlaystyle(AggressivePlaystyle.identifierS());
			//this.broadcaster.setCurrentPlaystyle(LauncherPlaystyle.identifierS());
		} catch (GameActionException e) {}
		
	}

	public void run() {
		
		boolean updatedBudgeting = false;
		try {
			this.broadcaster.newTurn();
								
			// update the new ore totals
			
			this.previousOreTotal = this.currentOreTotal;
			this.currentOreTotal = (int)this.robotController.getTeamOre();
			
			int currentOre = this.currentOreTotal;
			currentOre += this.broadcaster.oreSpentLastTurn();
			
			int oreMined = currentOre - this.previousOreTotal;
			
			this.oreMined += oreMined;
			this.oreMinedTurns += 1;
			
			// update the budgets if necessary
			if (this.oreMinedTurns > ORE_MINED_HOLD_TURNS) {
				
				this.currentPlaystyle().updateBudgeting(this.oreMined);
				
				updatedBudgeting = true;
				this.oreMined = 0;
				this.oreMinedTurns = 0;
				
			}
									
		}
		catch (GameActionException e) {}
		
		super.run();
		
		try {
			
			if (!this.attack() && !updatedBudgeting) {
				
				// check if we can deal damage with splash damage
				
				int towers = this.locationController.towerLocations().length; 
				if (towers >= 5) {
										
					MapLocation currentLocation = this.locationController.currentLocation();
					RobotInfo[] enemies = this.unitController.nearbyEnemies(HQ.type().sensorRadiusSquared + 50);
					for (RobotInfo enemy : enemies) {
						
						int distance = currentLocation.distanceSquaredTo(enemy.location);
						if (distance <= 64) {
							
							Direction direction = enemy.location.directionTo(currentLocation);
							MapLocation attackLocation = enemy.location.add(direction);
							
							if (this.robotController.canAttackLocation(attackLocation)) {
								
								this.robotController.attackLocation(attackLocation);
								break;
								
							}
							
						}
						
					}
					
				}
				
			}
						
			if (this.robotController.isCoreReady()) {
				
				if (this.broadcaster.robotCountFor(Beaver.type()) < 15) {
					
					this.trySpawn(Beaver.type());
					
				}
				
			}
						
			this.supplyController.transferSupplyIfPossible();
			
		} catch (GameActionException e) {
		}
		
		this.robotController.yield();
		
	}
	
	// MARK: Static Helpers
	
	public static int friendlyAttackRadiusSquared(int towers) {
		
        int attackRadius = HQ.type().attackRadiusSquared;
        if (towers >= 2) attackRadius = 35;
        return attackRadius;
		
	}
	
	public static int enemyAttackRadiusSquared(int towers) {

        int attackRadius = HQ.type().attackRadiusSquared;
        if (towers >= 2) attackRadius = 35;
        if (towers >= 5) attackRadius = 64;
        return attackRadius;
		
	}
	
	public static RobotType type() {
		return RobotType.HQ;
	}

}
