package team170;

import battlecode.common.*;

public class RobotBroadcaster {
	
	// definitions
	private static final int PLAYSTYLE_CHANNEL = 1;
	private static final int ORE_SPENT_INDEX = 4;
	private static final int ORE_SPENT_COPY_INDEX = 5;
	
	private static final int ROBOTS_STARTING_INDEX = 100;
	private static final int ROBOTS_COPY_INDEX = 200;
	private static final int ROBOTS_BUILDING_INDEX = 300;
	private static final int ROBOTS_BUDGET_INDEX = 400;
	
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
	
	public Boolean isRobotTypeCivic(RobotType type) {
		
		switch (type) {
			case HQ:
			case BARRACKS:
			case TOWER:
			case MINERFACTORY:
			case HELIPAD:
			case TANKFACTORY:
			case SUPPLYDEPOT:
			case AEROSPACELAB:
				return true;
			default: return false;
		}
		
	}
	
	public int budgetIndexForRobotType(RobotType type) {
		
		if (isRobotTypeCivic(type)) return 50;
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
		
		return budgetForType(RobotType.HQ);
		
	}
	
	public void incrementCivicBudget(int increment) throws GameActionException {
		
		this.incrementBudget(RobotType.HQ, increment);
		
	}
	
	public void decrementCivicBudget(int decrement) throws GameActionException {
		
		this.decrementBudget(RobotType.HQ, decrement);
		
	}
	
	// MARK: Robots
	
	public int robotCountFor(RobotType type) throws GameActionException {
		
		return this.livingRobotCountFor(type) + this.buildingRobotCountFor(type);
		
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
		
	}
	
	public void incrementLivingRobotCountFor(RobotType type) throws GameActionException {

		int channel = ROBOTS_COPY_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);
		
	}
	
	public int livingRobotCountFor(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_COPY_INDEX + this.incrementForRobot(type));
		
	}
	
	// MARK: Robots (Building)
	
	public void incrementBuildingRobotCountFor(RobotType type) throws GameActionException {

		int channel = ROBOTS_BUILDING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);
		
	}
	
	public void decrementBuildingRobotCountFor(RobotType type) throws GameActionException {

		int channel = ROBOTS_BUILDING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, Math.max(0, this.readBroadcast(channel) - 1));
		
	}
	
	public int buildingRobotCountFor(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_BUILDING_INDEX + this.incrementForRobot(type));
		
	}
	
	// MARK: Turns
	
	public void newTurn() throws GameActionException {

		// copy the robot counts
		for (int i = 0; i < 25; i++) {
			
			this.broadcast(ROBOTS_COPY_INDEX + i, this.readBroadcast(ROBOTS_STARTING_INDEX + i));
			this.broadcast(ROBOTS_STARTING_INDEX + i, 0);
			
		}
				
		// copy the ore spent
		this.broadcast(ORE_SPENT_COPY_INDEX, this.readBroadcast(ORE_SPENT_INDEX));
		this.broadcast(ORE_SPENT_INDEX, 0);
				
	}

}
