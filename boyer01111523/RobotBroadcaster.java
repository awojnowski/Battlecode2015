package boyer01111523;

import boyer01111523.movement.MovementController;
import battlecode.common.*;

public class RobotBroadcaster {
	
	// definitions
	private static final int PLAYSTYLE_CHANNEL = 1; // channel denotes the current playstyle
	private static final int ORE_SPENT_INDEX = 4; // amount of ore spent this turn (tally)
	private static final int ORE_SPENT_COPY_INDEX = 5; // amount of ore spent last turn
	private static final int ROBOTS_SPAWNED_TALLY_INDEX = 6; // amount of robots SPAWNED (not built) (tally)
	private static final int ROBOTS_SPAWNED_INDEX = 7; // amount of robots SPAWNED (spawned_index + building_index)
	private static final int MISSILE_DIRECTION_INDEX = 8; // the direction that the next spawned missile should go in
	
	private static final int ROBOTS_STARTING_INDEX = 100; // amount of robots reported this turn (tally)
	private static final int ROBOTS_COPY_INDEX = 200; // robot types reported last turn
	private static final int ROBOTS_BUILDING_INDEX = 300; // civic robot types reported as building
	private static final int ROBOTS_BUILDING_COUNTDOWN_INDEX = 400; // civic robot types reported as building
	private static final int ROBOTS_BUDGET_INDEX = 500; // robot budgets
	
	// required properties
	public RobotController robotController;
	
	public void broadcast(int channel, int data) throws GameActionException {
		
		this.robotController.broadcast(channel, data);
		
	}
	
	public int readBroadcast(int channel) throws GameActionException {
		
		return this.robotController.readBroadcast(channel);
		
	}
	
	// MARK: Playstyle
	
	public int currentPlaystyle() throws GameActionException {
		
		return this.readBroadcast(PLAYSTYLE_CHANNEL);
		
	}
	
	public void setCurrentPlaystyle(int identifier) throws GameActionException {
		
		this.broadcast(PLAYSTYLE_CHANNEL, identifier);
		
	}
	
	// MARK: Budgeting
	
	public Boolean isRobotTypeBuilding(RobotType type) {
		
		if (this.isRobotTypeCivic(type)) return true;
		if (type == SupplyDepot.type()) return true;
		return false;
		
	}
	
	public Boolean isRobotTypeCivic(RobotType type) {
		
		switch (type) {
			case HQ:
			case BARRACKS:
			case TOWER:
			case MINERFACTORY:
			case HELIPAD:
			case TANKFACTORY:
			case AEROSPACELAB:
				return true;
			default: return false;
		}
		
	}
	
	public int budgetIndexForRobotType(RobotType type) {
		
		if (this.isRobotTypeCivic(type)) return 50;
		return incrementForRobot(type);
		
	}
	
	public int budgetForType(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_BUDGET_INDEX + budgetIndexForRobotType(type));
		
	}
	
	public void setBudget(RobotType type, int budget) throws GameActionException {

		this.broadcast(ROBOTS_BUDGET_INDEX + budgetIndexForRobotType(type), Math.max(0, budget));
		
	}
	
	public void incrementBudget(RobotType type, int increment) throws GameActionException {

		this.setBudget(type, this.readBroadcast(ROBOTS_BUDGET_INDEX + budgetIndexForRobotType(type)) + increment);
		
	}
	
	public void decrementBudget(RobotType type, int decrement) throws GameActionException {

		this.setBudget(type, this.readBroadcast(ROBOTS_BUDGET_INDEX + budgetIndexForRobotType(type)) - decrement);
		
	}
	
	// MARK: Budget (Civic)
	
	public int civicBudget() throws GameActionException {
		
		return this.budgetForType(RobotType.HQ);
		
	}
	
	public void incrementCivicBudget(int increment) throws GameActionException {
		
		this.incrementBudget(RobotType.HQ, increment);
		
	}
	
	public void decrementCivicBudget(int decrement) throws GameActionException {
		
		this.decrementBudget(RobotType.HQ, decrement);
		
	}
	
	// MARK: Missiles
	
	public void setNextMissileDirection(Direction direction) throws GameActionException {
		
		this.broadcast(MISSILE_DIRECTION_INDEX, MovementController.directionToInt(direction));
		
	}
	
	public Direction nextMissileDirection() throws GameActionException {
		
		return MovementController.directionFromInt(this.readBroadcast(MISSILE_DIRECTION_INDEX));
		
	}
	
	// MARK: Robots
	
	public int robotCountFor(RobotType type) throws GameActionException {
		
		int count = this.livingRobotCountFor(type);
		if (this.isRobotTypeBuilding(type)) count += this.buildingRobotCountFor(type);
		return count;
		
	}
	
	public int totalSpawnedRobotCount() throws GameActionException {
		
		return this.readBroadcast(ROBOTS_SPAWNED_INDEX);
		
	}
	
	private int incrementForRobot(RobotType type) {
		
		return type.ordinal();
		
	}
	
	// MARK: Ore
	
	public void incrementSpentOre(int increment) throws GameActionException {

		this.broadcast(ORE_SPENT_INDEX, this.readBroadcast(ORE_SPENT_INDEX) + increment);
		
	}
	
	public int oreSpentLastTurn() throws GameActionException {
		
		return this.readBroadcast(ORE_SPENT_COPY_INDEX);
		
	}
	
	// MARK: Robots (Living)
	
	public void incrementLivingTalliedRobotCountFor(RobotType type) throws GameActionException {
		
		int channel = ROBOTS_STARTING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);

		if (!this.isRobotTypeCivic(type)) {
			
			this.broadcast(ROBOTS_SPAWNED_TALLY_INDEX, this.readBroadcast(ROBOTS_SPAWNED_TALLY_INDEX) + 1);
			
		}
		
	}
	
	public int livingRobotCountFor(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_COPY_INDEX + this.incrementForRobot(type));
		
	}
	
	// MARK: Robots (Building)
	
	public void beginBuildingRobot(RobotType type) throws GameActionException {
		
		if (!this.isRobotTypeBuilding(type)) return;

		int channel = ROBOTS_BUILDING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);

		channel = ROBOTS_BUILDING_COUNTDOWN_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + type.buildTurns + 2);
		
	}
	
	public void finishBuildingRobot(RobotType type) throws GameActionException {
		
		if (!this.isRobotTypeBuilding(type)) return;

		int channel = ROBOTS_BUILDING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, Math.max(0, this.readBroadcast(channel) - 1));
		
	}
	
	public int buildingRobotCountFor(RobotType type) throws GameActionException {
		
		if (!this.isRobotTypeBuilding(type)) return 0;
		return this.readBroadcast(ROBOTS_BUILDING_INDEX + this.incrementForRobot(type));
		
	}
	
	// MARK: Turns
	
	public void newTurn() throws GameActionException {

		// copy the robot counts
		for (int i = 0; i < 25; i++) {
			
			this.broadcast(ROBOTS_COPY_INDEX + i, this.readBroadcast(ROBOTS_STARTING_INDEX + i));
			this.broadcast(ROBOTS_STARTING_INDEX + i, 0);
			
		}

		this.broadcast(ROBOTS_SPAWNED_INDEX, this.readBroadcast(ROBOTS_SPAWNED_TALLY_INDEX));
		this.broadcast(ROBOTS_SPAWNED_TALLY_INDEX, 0);
		
		// figure out if any building buildings are written off
		for (int i = 0; i < 25; i++) {
			
			int total = this.readBroadcast(ROBOTS_BUILDING_COUNTDOWN_INDEX + i);
			if (total > 0) {
				
				total -= this.readBroadcast(ROBOTS_BUILDING_INDEX + i);
				if (total <= 0) {
							
					// we just decremented it to zero from >0 which means that
					// the counter never stopped so there should be a write off
					total = 0;
					this.broadcast(ROBOTS_BUILDING_INDEX + i, Math.max(0, this.readBroadcast(ROBOTS_BUILDING_INDEX + i) - 1));
					
				}
				this.broadcast(ROBOTS_BUILDING_COUNTDOWN_INDEX + i, total);
				
			}
			
		}
		
		// copy the ore spent
		this.broadcast(ORE_SPENT_COPY_INDEX, this.readBroadcast(ORE_SPENT_INDEX));
		this.broadcast(ORE_SPENT_INDEX, 0);
				
	}

}
