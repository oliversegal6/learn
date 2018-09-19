package threads.synchronization.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueue {
	
	static ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(50, true);
	//static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(50);	
	//static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();	// Integer.MAX_VALUE

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			new Producer(queue).start();
		}
		
		Thread.sleep(100);
		consume(queue);
	}
	
	public static void consume(ArrayBlockingQueue<Integer> queue) throws InterruptedException {
		while (true) {
			int num = queue.take();
			System.out.print("size: " + queue.size());
			System.out.println(", remaining capacity: " + queue.remainingCapacity());
			Thread.sleep(318);
		}
	}
}
