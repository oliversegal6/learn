package threads.safety.trouble;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter2 {

	static AtomicInteger counter = new AtomicInteger(0);
	
	/**
	 * <p> +2, counter should always be even number </p>
	 * */
	static synchronized public void add2() {
		counter.incrementAndGet();
		counter.incrementAndGet();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(()->{
			while (counter.intValue() <= 100_000) {
				// read
				if (counter.intValue() % 2 != 0) {
					System.out.println(counter.intValue());
					System.exit(0);
				}
			} 
			
			System.out.println(counter.intValue());
			System.exit(0);
		}).start();
		
		// write
		while (true) {
			add2();
		}
	}

}
