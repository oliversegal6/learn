package threads.synchronization.locks.shooting;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InterruptLongWait {
	
	Lock lock = new ReentrantLock();
	
	public void task1() {
		try {
			lock.lock();
			System.out.println("task1 start");
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println("task1 finished...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void task2() {
		try {
			lock.lockInterruptibly();
			System.out.println("task2 start");
			TimeUnit.SECONDS.sleep(1);
			System.out.println("task2 finished...");
		} catch (Exception e) {
			System.out.println("task2 got interrupted");
		} finally {
			if (lock.tryLock())
				lock.unlock();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		InterruptLongWait t = new InterruptLongWait();
		
		new Thread(t::task1, "task1").start();
		Thread task2 = new Thread(t::task2, "task2");
		task2.start();
		
		TimeUnit.SECONDS.sleep(2);
		task2.interrupt();
	}
}
