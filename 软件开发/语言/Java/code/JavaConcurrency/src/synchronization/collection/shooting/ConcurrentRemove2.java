package threads.synchronization.collection.shooting;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class ConcurrentRemove2 {
	
	static Queue<Integer> list = new ConcurrentLinkedQueue<>();
	
	public static void main(String[] args) {
		
		IntStream.range(0, 10000).forEach(list::add);
		
		for (int i=0; i<10; i++) {
			new Thread(()->{
				while (true) {

					Integer val = list.poll();
					if (val == null) {
						break;
					}
					list.remove(0);
				}
			}).start();
		}
	}
}
