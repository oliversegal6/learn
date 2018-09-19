package threads.safety.trouble;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class VTest {
	// any write 'happens-before' current read.
	private static boolean isXXX = false;
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch cd = new CountDownLatch(5);
		
		
		for (int i=0;i<5;i++) {
			new Thread(()->{
				while (true) {
					if (isXXX) {
						System.out.println(Thread.currentThread().getName());
						cd.countDown();
						break;
						/*try {
							Thread.sleep(1000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
				}
			}, "task-"+i).start();
		}
		
		new Thread(()->{
			isXXX = true;
		}).start();
		
		cd.await(60, TimeUnit.SECONDS);
		System.exit(0);
	}
}
