package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;


public class AccountServerImpl extends UnicastRemoteObject implements
		AccountServer, PeerCommunication {
	private static final long serialVersionUID = 1L;
	private static int serverID;
	private static int totalServerNum;
	private static Logger logger;
	private int totalTransaction = 0;

	public AccountServerImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean requestTransfer(int srcid, int dstid, int amount)
			throws RemoteException {
		if (peerServers.isEmpty()) {
			bindPeerServers();
		}
		Event e = new Event(serverID, srcid, dstid, totalServerNum,
				logicalClock.intValue());
		// logicalClock.addAndGet(1);
		addToEventQueue(e);
		return true;
	}

	/* Peer Communication Part */
	private static ArrayList<PeerCommunication> peerServers = new ArrayList<>();
	private AtomicLong logicalClock = new AtomicLong(0);
	private PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<Event>();

	public void addToEventQueue(Event e) throws RemoteException {
		logger.write(serverID + " CLNT-REQ " + logicalClock.get()
				+ e.getOpName() + e.getParameter());
		logicalClock.addAndGet(1);
		int minimumACK = Integer.MAX_VALUE;
		int minimumServerID = -1;
		eventQueue.add(e);
		for (PeerCommunication peerServer : peerServers) {
			if (peerServer.getID() != serverID) {
				AtomicLong timeStamp = peerServer.broadCastRequest(e,
						logicalClock);
				System.out.println("Here?");
				if (minimumACK > timeStamp.get()) {
					minimumACK = (int) timeStamp.get();
					minimumServerID = peerServer.getID();
				}
				logicalClock = new AtomicLong(Math.max(timeStamp.longValue(),
						logicalClock.longValue() + 1));
			}
		}

		checkQueueAndExecuteEvent(minimumServerID, minimumACK);
	}

	@Override
	public synchronized AtomicLong broadCastRequest(Event e,
			AtomicLong timeStamp) throws RemoteException {
		logger.write(serverID + " SRV-REQ " + timeStamp.get() + e.getOpName()
				+ e.getParameter() + "EventTimeStam: " + e.getTimestamp());

		logicalClock = new AtomicLong(Math.max(timeStamp.longValue(),
				logicalClock.longValue() + 1));
		eventQueue.add(e);
		return logicalClock;
	}

	private void checkQueueAndExecuteEvent(int srcServerID, long timeStamp)
			throws RemoteException {
		for (Iterator<Event> iterator = eventQueue.iterator(); iterator
				.hasNext();) {
			Event e = iterator.next();
			if (timeStamp > e.getTimestamp()) {
				executeEvent(e);
				iterator.remove();
				// logger.write("Process ID: "+ this.getID() +
				// " Queue time: "+this.eventQueue.peek().getTimestamp());
			}
		}
	}

	@Override
	public int getID() throws RemoteException {
		return serverID;
	}

	@Override
	public void halt() {
		for (PeerCommunication peer : peerServers) {
			try {
				if (peer.getID() != serverID)
					peer.haltFromServerZero();
			} catch (Exception e2) {

			}
		}

		try {
			haltFromServerZero();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void haltFromServerZero() throws RemoteException {
		while (!eventQueue.isEmpty()) {
			executeEvent(eventQueue.poll());
		}
		printAccInfo();
		System.exit(0);
	}

	private void printAccInfo() {
		Set<Integer> keySet = Storage.dataTable.keySet();
		System.out.println("Total Transaction:" + totalTransaction);
		System.out.println("Account Summary");
		for (int i = 0; i < 10; i++) {
			System.out
					.println(i + ": " + Storage.dataTable.get(i).getBalance());
		}
	}

	private void executeEvent(Event e) {
		totalTransaction++;
		logger.write(serverID + " PROCESS " + logicalClock.get() + e.toString());
		int srcid = e.getSrcID();
		int dstid = e.getDstID();
		Account src = getAccountByID(srcid);
		Account dst = getAccountByID(dstid);

		if (!src.withdraw(10)) {
			logger.write("Transfer: Account No." + srcid + " to " + dstid
					+ ": Not sufficient fund on source account to transfer");
		}

		dst.deposit(10);
		logger.write("Transfer: Source id is " + srcid + ". Destination id is "
				+ dstid + ":Success");
	}

	private Account getAccountByID(int id) {
		return Storage.dataTable.get(id);
	}

	private static void initAccountServer() {
		for (int i = 0; i < 10; i++) {
			Account newAccount = new Account("1", "2", "3");
			newAccount.deposit(1000);
			int nid = Storage.AccIDCounter;
			Storage.dataTable.put(nid, newAccount);
			Storage.AccIDCounter++;
		}
	}

	private static void bindPeerServers() {
		PeerCommunication pc;
		try {
			Set<Integer> keySet = Constant.infoMap.keySet();
			for (int key : keySet) {
				serverInfo info = Constant.infoMap.get(key);
				String hostname = info.Hostname;
				int portNum = info.clientRequestPort;
				pc = (PeerCommunication) Naming.lookup("//" + hostname + ":"
						+ portNum + "/AccountServer" + key);
				peerServers.add(pc);
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		if (args.length != 2) {
			throw new RuntimeException(
					"Syntax:AccountServer <ID> <PathOfConfigurationFile>");
		}

		logger = new Logger("server_log.txt");
		serverID = Integer.valueOf(args[0]);

		initAccountServer();
		Constant.constantGenerator(args[1]);
		totalServerNum = Constant.infoMap.size();
		AccountServerImpl accIm = new AccountServerImpl();
		Registry AccoutServerRegistry = LocateRegistry
				.getRegistry(Constant.ACC_SVR_PORT);
		String name = "AccountServer" + serverID;
		AccoutServerRegistry.bind(name, accIm);
	}
}