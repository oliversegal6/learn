package threads.synchronization.collection;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
	
	private BlockingQueue<Integer> queue;
	
	public Producer(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				queue.put(new Random().nextInt(100));
				sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println("Error in Producer --> " + e.getMessage());
		}
	}
}
