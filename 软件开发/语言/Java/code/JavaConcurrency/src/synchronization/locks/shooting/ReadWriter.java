package threads.synchronization.locks.shooting;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriter {
	
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private static String data = "aaa";
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(()->{ write("AAA");
		}).start();
		
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
			lock.readLock().lock();;
			Thread.sleep(1000);
			result = data;
			System.out.println("read: " + result);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			lock.readLock().unlock();
		}
			
		return result;
	}
	
	public static String write(String val) {
		try {
			lock.writeLock().lock();
			data = val;
			System.out.println("write: " + val);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			lock.writeLock().unlock();
		}
		
		return data;
	}
	
}
