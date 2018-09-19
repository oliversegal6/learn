package threads.synchronization.collection;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class Priority {
	public static void main(String[] args) throws InterruptedException {
		PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<Integer>(10, (a,b)->{return a>b?-1:(a<b?1:0);});
		
		for (int i = 0; i<10; i++) {
			queue.put(new Random().nextInt(100));
		}
		
		while (queue.size()>0) {
			System.out.println(queue.take());
		}
	}
}
