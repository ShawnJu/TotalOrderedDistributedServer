package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountServer extends Remote {
	 boolean requestTransfer(int srcid, int dstid, int amount)
			throws RemoteException, InterruptedException;

	 void halt() throws RemoteException;
}