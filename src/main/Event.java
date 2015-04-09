package main;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Event implements Comparable<Event>, Serializable {

	private static final long serialVersionUID = 34L;
	private int timestamp;
	private int processID;
	private int numPeerProceed;
	private int srcID;
	private int dstID;
	private Set<Integer> serverSet = new HashSet<Integer>();
	public Set<Integer> getServerSet() {
		return serverSet;
	}

	private boolean isHalt;

	public Event(int processID, int srcID, int dstID, int totalServerNum, int timestamp) {
		this.processID = processID;
		this.srcID = srcID;
		this.dstID = dstID;
		this.numPeerProceed = 1;
		this.timestamp = timestamp;
		for (int i = 0; i < totalServerNum; i++) {
			if (i != processID)
			serverSet.add(i);
		}
	}

	public Event(boolean isHalt) {
		this.isHalt = isHalt;
	}

	public boolean isHalt() {
		return isHalt;
	}

	public int getSrcID() {
		return srcID;
	}

	public int getDstID() {
		return dstID;
	}

	public int getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getProcessID() {
		return processID;
	}

	public int getNumPeerProceed() {
		return numPeerProceed;
	}

	public void peerACK(int serverID) {
		if (serverSet.contains(serverID)) {
			numPeerProceed++;
			serverSet.remove(serverID);
		}
	}

	public String getOpName() {
		return isHalt ? "HALT" : "TRANSFER";
	}

	public String getParameter() {
		return isHalt() ? "" : " SrcID : " + srcID + " DstID : " + dstID;
	}

	@Override
	public int compareTo(Event e) {
		return (this.timestamp > e.getTimestamp())
				|| (this.timestamp == e.getTimestamp() && this.processID < e
						.getProcessID()) ? 1 : -1;
	}
	
	public String toString() {
		return "sender id: " + processID + getParameter() + " " + serverSet.toString();
	}
}
