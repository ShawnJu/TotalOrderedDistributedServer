package main;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientB implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		if (args.length != 1)
			throw new RuntimeException(
					"Syntax: ClientB <<PathOfConfigurationFile>>");
		Constant.constantGenerator(args[0]);

		AccountServer firstServer = null;
		serverInfo info = Constant.infoMap.get(0);
		try {
			firstServer = (AccountServer) Naming.lookup("//"
					+ info.Hostname + ":" + info.clientRequestPort + "/AccountServer0");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
		Logger log = new Logger("client_log.txt");

//		/* Step 1 */
		ArrayList<Thread> arr_thread = new ArrayList<>();
		Iterator<serverInfo> itr = Constant.infoMap.values().iterator();
		while(itr.hasNext()){
			info = itr.next();
			ClientBWorker worker = new ClientBWorker(info.Hostname,
					info.clientRequestPort, log, info.ID);
			Thread t = new Thread(worker);
			arr_thread.add(t);
			log.write("Worker " + info.ID + ":start");
			t.start();
		}

		/* Step 2 */

		for (int i = 0; i< arr_thread.size(); i++) {
			try {
				arr_thread.get(i).join();
				log.write("Worker " + i + ":end");
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		/* Step 3 */
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			log.write("HALT");
			firstServer.halt();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
