package threads.basics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * JDK cases: FinalizerThread, ReferenceHandler
 * */
public class T_Daemon {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Runnable service = ()->{
			while (true) {
				try {
					String name = Thread.currentThread().getName();
					Thread.sleep(2000);
					System.out.println(name + " is running...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		for (int i=0;i<5;i++) {
			Thread t = new Thread(service, "service-"+i);
			t.setDaemon(true); // convert user thread to daemon thread
			
			TimeUnit.SECONDS.sleep(1);
			t.start();
		}

		// main exit
		System.in.read();
	}
}