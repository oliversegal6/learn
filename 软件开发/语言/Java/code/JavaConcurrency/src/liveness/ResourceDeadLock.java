package threads.liveness;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ResourceDeadLock {
	
	/** dummy DB connection pools */
	static Queue<Integer> poolA = new LinkedBlockingQueue<>(10);
	static Queue<Integer> poolB = new LinkedBlockingQueue<>(10);
	
	static {
		for (int i=0; i<10; i++) {
			poolA.add(i);
			poolB.add(i);
		}
	}
	
	public static void main(String[] args) {
		
		/** consume DB connections */
		for (int i=0; i<9; i++) {
			System.out.println("Take connection from PoolA: " + poolA.poll());
			System.out.println("Take connection from PoolB: " + poolB.poll());
		}
		
		new Thread(()->{
			String name = Thread.currentThread().getName();
			System.out.println(name + " take connection from PoolA: " + poolA.poll());
			
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println(name + " is working ...");
				
				while (poolB.isEmpty()) {
					TimeUnit.SECONDS.sleep(2);
					System.out.println(name + " is waiting on PoolA ...");
				}
				
				System.out.println(name + " take connection from PoolB: " + poolB.poll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "task-1").start();
		
		new Thread(()->{
			String name = Thread.currentThread().getName();
			System.out.println(name + " take connection from PoolA: " + poolB.poll());
			
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println(name + " is working ...");
				
				while (poolA.isEmpty()) {
					TimeUnit.SECONDS.sleep(2);
					System.out.println(name + " is waiting on PoolB ...");
				}
				
				System.out.println(name + " take connection from PoolB: " + poolA.poll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "task-2").start();
		
	}
	
}
