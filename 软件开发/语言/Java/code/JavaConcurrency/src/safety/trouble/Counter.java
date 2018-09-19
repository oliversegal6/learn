package threads.safety.trouble;

import java.util.concurrent.CountDownLatch;

/**
 * == time ==================================================
 * 	T1		read 0			+1			write 1
 * 	T2			read 0			+1			write 1
 * race condition/thread interference
 * */
public class Counter {
	static int counter = 0;
	
	public void add() {
		counter++;
	}
	
	public static void main(String[] args) throws InterruptedException {
		int nTask = 5;
		CountDownLatch cd = new CountDownLatch(nTask);
		
		Counter c = new Counter();
		
		for (int i = 0; i < nTask; i++) {
			new Thread(()->{
				for (int j=0; j<10_000; j++) {
					c.add();
				}
				cd.countDown();
			}).start();
		}
		
		cd.await();
		System.out.println(counter);
	}
}
