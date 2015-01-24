package team170.supply;

import battlecode.common.*;
import team170.*;

public class SupplyController {
	
	public Robot robot;
	
	// MARK: Supply
	
	public void transferSupplyIfPossible() throws GameActionException {
		
		double supplyLevel = this.robot.robotController.getSupplyLevel();
		double minimumSupplyLevel = this.minimumSupplyForType(this.robot.type);
		if (supplyLevel < minimumSupplyLevel) return;
		
		double lowestSupply = supplyLevel;
		
		RobotInfo[] allies = this.robot.unitController.nearbyAllies(GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED);
		MapLocation targetLocation = null;
		for (RobotInfo ally : allies) {
			
			// check if this type can receive supply
			if (!this.canTypeReceiveSupply(ally.type)) continue;			
			if (ally.supplyLevel < lowestSupply) {
				
				lowestSupply = ally.supplyLevel;
				targetLocation = ally.location;
				
			}
			
		}

		if (targetLocation != null) {

			double transferableSupply = supplyLevel - minimumSupplyLevel;
			if (this.robot.type == HQ.type()) {
				
				// give it all away
				
			} else {
				
				transferableSupply = transferableSupply / 2;
				
			}
			this.robot.robotController.transferSupplies((int)transferableSupply, targetLocation);
			
		}
		
	}
	
	// MARK: Supply Helpers
	
	private Boolean canTypeReceiveSupply(RobotType type) {
		
		if (Clock.getRoundNum() > 100 && type == Beaver.type()) return false;
		return type.needsSupply();
		
	}
	
	private double minimumSupplyForType(RobotType type) {
		
		if (type == HQ.type()) return 0;
		return 500;
		
	}

}
