package threads.liveness;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Starvation {
	
	Lock lock = new ReentrantLock();
	
	public void work() {
		try {
			lock.lock();
			TimeUnit.SECONDS.sleep(3);
			System.out.println(Thread.currentThread().getName() + " is running ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void work2() {
		try {
			String name = Thread.currentThread().getName();
			while (!lock.tryLock(2, TimeUnit.SECONDS)){
				System.out.println(name + " failed to acquire the lock");
			}
			System.out.println(name + " is running ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		Starvation s = new Starvation();
		
		new Thread(()->{
			for (int i=0; i<100; i++) {
				s.work();
			}
		}, "task-1").start();
		
		new Thread(()->{
			s.work2();
		}, "task-2").start();
	
	}
	
}