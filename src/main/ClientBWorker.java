package main;

import java.rmi.Naming;
import java.util.Random;

public class ClientBWorker implements Runnable {
	java.util.Calendar cal = java.util.Calendar.getInstance();
	private String hostName;
	private int workerNum;
	private Logger logger;
	private int serverPort;

	public ClientBWorker(String hostName, int serverPort, Logger log,
			int workerNum) {
		this.serverPort = serverPort;
		this.hostName = hostName;
		this.workerNum = workerNum;
		this.logger = log;
	}

	@Override
	public void run() {
		try {
			AccountServer server = (AccountServer) Naming.lookup("//"
					+ this.hostName + ":" + this.serverPort + "/AccountServer"
					+ this.workerNum);
			for (int i = 0; i < 100; i++) {
				int srcid = getRandomNumber();
				int dstid = getRandomNumber();
				this.logger.write(workerNum+ " REQ " +cal.getTimeInMillis() + " FROM srcID : " + srcid + " TO dstID " + dstid );
				Boolean ret =server.requestTransfer(srcid, dstid, 10);
				if(ret)
					this.logger.write(workerNum+ " RSP " +cal.getTimeInMillis() + " SUCCESS " );
				else
					this.logger.write(workerNum+ " RSP " +cal.getTimeInMillis() + " FAILED " );


			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private int getRandomNumber() {
		Random rand = new Random();

		int randomNum = rand.nextInt(10);

		return randomNum;
	}
}
