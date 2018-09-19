package threads.safety.shooting;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter2 {

	static AtomicInteger counter = new AtomicInteger(0);
	
	synchronized static void add2() {
		counter.incrementAndGet();
		counter.incrementAndGet();
	}
	
	synchronized static int getcounter() {
		return counter.intValue();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(()->{
			while (getcounter() <= 100_000) {
				if (getcounter() % 2 != 0) {
					System.out.println(getcounter());
					System.exit(0);
				}
			} 
			
			System.out.println(getcounter());
			System.exit(0);
		}).start();
		
		while (true) {
			add2();
		}
	}

}
