package threads.synchronization.locks.trouble;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairLock {
	
	Lock lock = new ReentrantLock(true);
	
	public void work() {
		for (int i = 0; i < 100; i++) {
			lock.lock();
			try {
				Thread.sleep(1);
				System.out.println(Thread.currentThread().getName() + " is working ...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		FairLock f = new FairLock();
		
		new Thread(f::work, "worker1").start();
		new Thread(f::work, "worker2").start();
	}
}
