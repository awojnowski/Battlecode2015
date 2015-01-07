package team170;

import battlecode.common.*;

public class RobotBroadcaster {
	
	// definitions
	private static final int PLAYSTYLE_CHANNEL = 1;
	private static final int CIVIC_BUDGET_CHANNEL = 2;
	
	private static final int ROBOTS_STARTING_INDEX = 100;
	private static final int ROBOTS_COPY_INDEX = 200;
	
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
	
	// MARK: Civic Budget
	
	public int currentCivicBudget() throws GameActionException {
		
		return this.readBroadcast(CIVIC_BUDGET_CHANNEL);
		
	}
	
	public void setCurrentCivicBudget(int budget) throws GameActionException {

		this.broadcast(CIVIC_BUDGET_CHANNEL, budget);
		
	}
	
	public void incrementCurrentCivicBudget(int increment) throws GameActionException {

		this.setCurrentCivicBudget(this.readBroadcast(CIVIC_BUDGET_CHANNEL) + increment);
		
	}
	
	public void decrementCurrentCivicBudget(int decrement) throws GameActionException {

		this.setCurrentCivicBudget(this.readBroadcast(CIVIC_BUDGET_CHANNEL) - decrement);
		
	}
	
	// MARK: Robots
	
	public void resetRobotCounts() throws GameActionException {
		
		for (int i = 0; i < 25; i++) {
			
			this.broadcast(ROBOTS_COPY_INDEX + i, this.readBroadcast(ROBOTS_STARTING_INDEX + i));
			this.broadcast(ROBOTS_STARTING_INDEX + i, 0);
			
		}
		
	}
	
	public void incrementTalliedRobotCountFor(RobotType type) throws GameActionException {
		
		int channel = ROBOTS_STARTING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);
		
	}
	
	public void incrementRobotCountFor(RobotType type) throws GameActionException {

		int channel = ROBOTS_COPY_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);
		
	}
	
	public int robotCountFor(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_COPY_INDEX + this.incrementForRobot(type));
		
	}
	
	private int incrementForRobot(RobotType type) {
		
		return type.ordinal();
		
	}

}
