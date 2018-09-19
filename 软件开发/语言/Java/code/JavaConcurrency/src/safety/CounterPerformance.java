package threads.safety;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class CounterPerformance {
	
	static long counter;
	static AtomicLong counter2 = new AtomicLong();
	static LongAdder counter3 = new LongAdder();
	
	synchronized static void add() {
		counter++;
	}
	
	public static void main(String[] args) throws InterruptedException {
		test(()-> add());
		test(()-> counter2.incrementAndGet());
		test(()-> counter3.increment());
	}
	
	private static void test(Runnable task) {
		CountDownLatch cd = new CountDownLatch(10);
		
		long time = System.currentTimeMillis();
		for (int i=0; i<10; i++) {
			new MyTask(cd, task).start();
		}
		
		try {
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("time: " + (System.currentTimeMillis() - time));
	}
	
	static class MyTask extends Thread {
		private CountDownLatch cd;
		public MyTask(CountDownLatch cd, Runnable task) {
			super(task);
			this.cd = cd;
		}
		
		@Override
		public void run() {
			for (int j=0;j<10_000;j++) {
				super.run();
			}
			cd.countDown();
		}
	}
}
