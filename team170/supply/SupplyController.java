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
		RobotInfo targetAlly = null;
		for (RobotInfo ally : allies) {
			
			// check if this type can receive supply
			if (!this.canTypeReceiveSupply(ally.type)) continue;
			if (this.robot.type == Drone.type() && ally.supplyLevel > 0) continue;
			if (ally.supplyLevel < lowestSupply) {
				
				lowestSupply = ally.supplyLevel;
				targetAlly = ally;
				
			}
			
		}

		if (targetAlly != null) {

			double transferableSupply = supplyLevel - minimumSupplyLevel;
			if (this.robot.type == HQ.type()) {
				
				// check if we have a supply drone so we limit supply to other units
				if (this.robot.broadcaster.robotCountFor(Drone.type()) > 0 && targetAlly.type != Drone.type()) {
					
					transferableSupply = 1000;
					
				}
				
			} else if (this.robot.type == Drone.type()) {
				
				transferableSupply = Math.max(transferableSupply, 1000);
				
			} else {
				
				transferableSupply = transferableSupply / 2;
				
			}
			this.robot.robotController.transferSupplies((int)transferableSupply, targetAlly.location);
			
		}
		
	}
	
	// MARK: Supply Helpers
	
	private Boolean canTypeReceiveSupply(RobotType type) {
		
		if (Clock.getRoundNum() > 100 && type == Beaver.type()) return false;
		if (type == Drone.type()) {
			
			if (this.robot.type == HQ.type()) return true;
			return false;
			
		}
		return type.needsSupply();
		
	}
	
	private double minimumSupplyForType(RobotType type) {
		
		if (type == HQ.type()) return 0;
		return 500;
		
	}

}
