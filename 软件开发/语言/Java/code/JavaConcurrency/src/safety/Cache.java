package threads.safety;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Cache {
	
	private static AtomicInteger number = new AtomicInteger(2);
	private static AtomicInteger result = new AtomicInteger(4);
	
	static ExecutorService pool = Executors.newFixedThreadPool(20);
	
	public static void main(String[] args) throws InterruptedException {
		
		for (int i = 0; i < 100; i++) {
			pool.submit(()->{
				while (!Thread.interrupted()) {
					compute(new Random().nextInt(10));
				}
			});
		}
		
		Thread.sleep(3000);
		pool.shutdownNow();
		
		System.out.println(number.intValue());
		System.out.println(result.intValue());
	}
	
	public static int compute(int n) {
		if (n == number.intValue()) {
			return result.intValue();
		} else {
			return square(n);
		}
	}
	
	private synchronized static int square(int n) {
		number.set(n);
		int a = number.intValue();
		a *= a;
		result.set(a);
		return result.intValue();
	}
	
}
