package main;

/**
 * Created by ShangJu on 4/5/15.
 */
public abstract class Message {
    protected int timestamp;
    protected int processID;
    protected int numPeerProceed;
    protected int srcID;
    protected int dstID;
    protected int amount;
    public abstract int getSrcID();
    public abstract int getDstID();
    public abstract int getTimestamp();
    public abstract int getProcessID();
}
