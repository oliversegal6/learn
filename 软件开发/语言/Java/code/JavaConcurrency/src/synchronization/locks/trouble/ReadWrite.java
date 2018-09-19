package threads.synchronization.locks.trouble;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWrite {
	
	private static ReentrantLock lock = new ReentrantLock();
	private static String data = "aaa";
	
	public static void main(String[] args) throws InterruptedException {
		// 1 write
		new Thread(()->{ write("AAA"); }).start();
		
		// 10 reads
		CountDownLatch cd = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				read();
				cd.countDown();
			}).start();
		}
		
		long time = System.currentTimeMillis();
		cd.await();
		System.out.println("time: " + (System.currentTimeMillis() - time));
	}
	
	public static String read() {
		String result = null;
		try {
			lock.lock();
			Thread.sleep(1000);
			result = data;
			System.out.println("read: " + result);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			lock.unlock();
		}
			
		return result;
	}
	
	public static String write(String val) {
		try {
			lock.lock();
			data = val;
			System.out.println("write: " + val);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			lock.unlock();
		}
		
		return data;
	}
	
}
