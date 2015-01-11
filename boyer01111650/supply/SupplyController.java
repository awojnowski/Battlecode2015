package boyer01111650.supply;

import battlecode.common.*;
import boyer01111650.*;

public class SupplyController {
	
	public Robot robot;
	
	// MARK: Supply
	
	public void transferSupplyIfPossible() throws GameActionException {
		
		double supplyLevel = this.robot.robotController.getSupplyLevel();
		double minimumSupplyLevel = this.minimumSupplyForType(this.robot.type);
		if (supplyLevel < minimumSupplyLevel) return;
		
		double transferableSupply = supplyLevel - minimumSupplyLevel;
		
		RobotInfo[] allies = this.robot.unitController.nearbyAllies();
		double lowestSupply = supplyLevel;
		
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
			
			this.robot.robotController.transferSupplies((int)(transferableSupply / 2), targetLocation);
			
		}
		
	}
	
	// MARK: Supply Helpers
	
	private Boolean canTypeReceiveSupply(RobotType type) {
		
		return type.needsSupply();
		
	}
	
	private double minimumSupplyForType(RobotType type) {
		
		if (type == HQ.type()) return 0;
		return 500;
		
	}

}
