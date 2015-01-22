package team170.queue;

import battlecode.common.GameActionException;
import team170.broadcaster.*;

public class Queue {
	
	private final int countOffset = 0;
	private final int queueOffset = 1;
	private final int queueIndex = 2;

	public Broadcaster broadcaster;
	public int startingIndex = 10000;
	
	public Queue() {
		
		
		
	}
	
	// MARK: Manipulation
	
	public void enqueue(int value) throws GameActionException {
		
		this.broadcaster.broadcast(this.nextQueueIndex(), value);
		this.setCount(this.count() + 1);
		this.incrementQueueOffset();
		
	}
	
	public int dequeue() throws GameActionException {
		
		if (this.count() <= 0) return Integer.MAX_VALUE;

		int value = this.broadcaster.readBroadcast(this.nextQueueIndex() - this.count());
		this.setCount(this.count() - 1);
		return value;
		
	}
	
	// MARK: Offsets
	
	public void incrementQueueOffset() throws GameActionException {

		this.broadcaster.broadcast(this.startingIndex + this.queueOffset, this.broadcaster.readBroadcast(this.startingIndex + this.queueOffset) + 1);
		
	}
	
	public int queueOffset() throws GameActionException {
		
		return this.broadcaster.readBroadcast(this.startingIndex + this.queueOffset);
		
	}
	
	public int nextQueueIndex() throws GameActionException {

		return this.startingIndex + this.queueIndex + this.queueOffset();
		
	}
	
	// MARK: Getters & Setters
	
	public void setCount(int count) throws GameActionException {

		this.broadcaster.broadcast(this.startingIndex + this.countOffset, count);
		
	}
	
	public int count() throws GameActionException {
		
		return this.broadcaster.readBroadcast(this.startingIndex + this.countOffset);
		
	}

}
