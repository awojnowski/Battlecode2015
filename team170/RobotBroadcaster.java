package team170;

import battlecode.common.*;

public class RobotBroadcaster {
	
	// definitions
	private static final int PLAYSTYLE_CHANNEL = 1;
	
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

}
