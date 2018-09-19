package threads.synchronization.locks.shooting;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LongWait {
	
	private static ReentrantLock lock = new ReentrantLock();
	
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
	
	public static void retrieveData() {
		try {
			lock.lock();
			/** time-consuming */
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void writeData(String val) {
		boolean acquired = false;
		try {
			if (acquired = lock.tryLock(2, TimeUnit.SECONDS)) {
				data += val;
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			if (acquired)
				lock.unlock();
		}
	}
	
	public static void doSthElse() {
		System.out.println("do sth else ...");
	}
		
}
