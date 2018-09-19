package threads.safety.atomicity.trouble;

import java.util.concurrent.atomic.AtomicInteger;

public class CompareSync {
	
	static int counter = 0;
	static AtomicInteger aCounter = new AtomicInteger(0);
	
	synchronized public void add() {
		counter++;
	}
	
	public void add2() {
		aCounter.incrementAndGet();
	}
	
	public static void main(String[] args) {
		CompareSync cs = new CompareSync();
		calcTime(cs::add);
		calcTime(cs::add2);
	}
	
	private static void calcTime(Runnable task) {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 1000_0000; i++) {
			task.run();
		}
		System.out.println(System.currentTimeMillis() - time);
	}
}
