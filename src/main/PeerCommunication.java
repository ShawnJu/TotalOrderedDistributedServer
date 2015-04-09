package main;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicLong;


public interface PeerCommunication extends Remote {
//	public void addToEventQueue(Event e) throws RemoteException;
	
	public AtomicLong broadCastRequest(Event t, AtomicLong timeStamp) throws RemoteException;
	
	public int getID() throws RemoteException;
	
	public void haltFromServerZero() throws RemoteException;
}
