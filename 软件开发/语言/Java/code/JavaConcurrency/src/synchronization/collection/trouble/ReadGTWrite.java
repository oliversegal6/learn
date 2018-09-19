package threads.synchronization.collection.trouble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;


public class ReadGTWrite {
	
	static List<Integer> list = new ArrayList<>();
	
	public static void main(String[] args) throws InterruptedException {
		ReadGTWrite r = new ReadGTWrite();
		Random random = new Random();
		CountDownLatch latch = new CountDownLatch(12);
		
		IntStream.range(0, 1000).forEach(list::add);
		//List<Integer> syncList = Collections.synchronizedList(list);
		CopyOnWriteArrayList<Integer> cow = new CopyOnWriteArrayList<>(list);
		
		long time = System.currentTimeMillis();
		
		for (int i=0; i<100; i++) {
			new Thread(()->{
				for (int j=0;j<10000;j++) {
					cow.get(random.nextInt(1000));
				}
				latch.countDown();
			}).start();
		}
		
		for (int i=0; i<5; i++) {
			new Thread(()->{
				for (int j=0;j<100;j++) {
					cow.add(random.nextInt(1000));
				}
				latch.countDown();
			}).start();
		}
		
		latch.await();
		
		System.out.println(System.currentTimeMillis() - time);
	}
}
