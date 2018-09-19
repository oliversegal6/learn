package threads.synchronization.locks.trouble;

import java.util.concurrent.TimeUnit;


public class LongWait {
	
	static String data = "hehe~ ";
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(()-> retrieveData()).start();
		
		new Thread(()-> {
			writeData("666");
			doSthElse();
		}).start();
		
		while (true) {
			Thread.sleep(3000);
			System.out.println(data);
		}
	}
	
	public synchronized static void retrieveData() {
		try {
			/** time-consuming */ 
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void writeData(String val) {
		data += val;
	}
	
	public static void doSthElse() {
		System.out.println("do sth else ...");
	}
		
}
