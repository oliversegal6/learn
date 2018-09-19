package threads.synchronization.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


public class C_ConcurrentHashMap {
	public static void main(String[] args) throws InterruptedException {
		//Map<String, String> map = new Hashtable<>();
		Map<String, String> map = new ConcurrentHashMap<>();

		CountDownLatch latch = new CountDownLatch(10);
		
		long time = System.currentTimeMillis();
		for (int i=0; i<30; i++) {
			new Thread(()->{
				for (int j=0; j<10_0000; j++) {
					map.put("k"+j, "v"+j);
				}
				latch.countDown();
			}).start();
		}
		
		latch.await();
		System.out.println(System.currentTimeMillis()-time);
	}
}
