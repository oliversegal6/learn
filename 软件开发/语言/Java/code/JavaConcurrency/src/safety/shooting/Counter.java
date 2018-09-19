package threads.safety.shooting;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	
	static int counter = 0;
	
	synchronized public void add() {
		counter++;
	}
	
	static AtomicInteger aCounter = new AtomicInteger(0);
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		CyclicBarrier barrier = new CyclicBarrier(6);
		
		Counter c = new Counter();
		
		long time = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			new Thread(()->{
				for(int j=0; j<20_000; j++) {
					aCounter.incrementAndGet();
				}
				try {
					barrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		barrier.await();
		System.out.println("Atomic Counter: " + aCounter.intValue() + ", time: " + (System.currentTimeMillis() - time));
		
		barrier.reset();
		for (int i = 0; i < 5; i++) {
			new Thread(()->{
				for(int j=0; j<20_000; j++) {
					c.add();
				}
				try {
					barrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		barrier.await();
		System.out.println("Synchronized Counter: " + counter + ", time: " + (System.currentTimeMillis() - time));
		
	}
	
}
