package team170;

import battlecode.common.*;

public class RobotBroadcaster {
	
	// definitions
	private static final int PLAYSTYLE_CHANNEL = 1;
	private static final int ROBOTS_STARTING_INDEX = 100;
	
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
	
	// MARK: Robots
	
	public void resetRobotCounts() throws GameActionException {
		
		for (int i = 0; i < 25; i++) {
			
			this.broadcast(ROBOTS_STARTING_INDEX + i, 0);
			
		}
		
	}
	
	public void incrementRobotCount(RobotType type) throws GameActionException {
		
		int channel = ROBOTS_STARTING_INDEX + this.incrementForRobot(type);
		this.broadcast(channel, this.readBroadcast(channel) + 1);
		
	}
	
	public int robotCountFor(RobotType type) throws GameActionException {
		
		return this.readBroadcast(ROBOTS_STARTING_INDEX + this.incrementForRobot(type));
		
	}
	
	public int incrementForRobot(RobotType type) {
		
		switch (type) {
			case AEROSPACELAB: return 1;
			case BARRACKS: return 2;
			case BASHER: return 3;
			case BEAVER: return 4;
			case COMMANDER: return 5;
			case COMPUTER: return 6;
			case DRONE: return 7;
			case HANDWASHSTATION: return 8;
			case HELIPAD: return 9;
			case HQ: return 10;
			case LAUNCHER: return 11;
			case MINER: return 12;
			case MINERFACTORY: return 13;
			case MISSILE: return 14;
			case SOLDIER: return 15;
			case SUPPLYDEPOT: return 16;
			case TANK: return 17;
			case TANKFACTORY: return 18;
			case TECHNOLOGYINSTITUTE: return 19;
			case TOWER: return 20;
			case TRAININGFIELD: return 21;
		}
		return 0;
		
	}

}
