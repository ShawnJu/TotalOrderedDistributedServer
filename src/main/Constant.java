package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Constant {

	static final int ACC_SVR_PORT = 8000;
	static final int PEER_COMM_PORT = 8001;

	public final static Map<Integer, serverInfo> infoMap = new HashMap<Integer, serverInfo>();

	
	public static void constantGenerator(String path) {
		try {
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(fileReader);

			String line = null;

			while ((line = br.readLine()) != null) {
				String[] temp = line.split("\\s+");
				infoMap.put(Integer.valueOf(temp[1]), new serverInfo(temp[0],
						Integer.valueOf(temp[1]), Integer.valueOf(temp[2]),
						Integer.valueOf(temp[3])));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

class serverInfo {
	String Hostname;
	int ID;
	int clientRequestPort;
	int peerRequestPort;

	serverInfo(String Hostname, int ID, int clientRequestPort,
			int peerRequestPort) {
		this.Hostname = Hostname;
		this.ID = ID;
		this.clientRequestPort = clientRequestPort;
		this.peerRequestPort = peerRequestPort;
	}

}
