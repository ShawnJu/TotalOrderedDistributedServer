package main;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Logger {
	private BufferedWriter logOut = null;
	
	public Logger(String name) {
		try {
			logOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name, false)));
		} catch (Exception e) {
			System.err.println("FailedOnCreateFiles");
			e.printStackTrace();
		}
	}
	
	public synchronized void write(String msg) {
		try {
			System.out.println("[INFO] "+ msg);
			logOut.write(msg + "\n");
			logOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException {
		logOut.close();
	}

}
