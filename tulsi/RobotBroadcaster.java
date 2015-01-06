package tulsi;

import battlecode.common.*;

public class RobotBroadcaster {

	public RobotController robotController;
	
	public void broadcast(int channel, int data) throws GameActionException {
		
		this.robotController.broadcast(channel, data);
		
	}
	
	public int readBroadcast(int channel) throws GameActionException {
		
		return this.robotController.readBroadcast(channel);
		
	}

}
