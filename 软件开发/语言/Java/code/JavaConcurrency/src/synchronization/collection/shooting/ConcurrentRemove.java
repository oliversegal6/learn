package threads.synchronization.collection.shooting;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class ConcurrentRemove {
	
	static List<Integer> list = new LinkedList<>();
	
	public static void main(String[] args) {
		
		IntStream.range(0, 10000).forEach(list::add);
		
		for (int i=0; i<10; i++) {
			new Thread(()->{
				synchronized (list) {
					while (list.size() > 0) { // check
						try {
							Thread.sleep(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// then act
						list.remove(0);
					}
				}
			}).start();
		}
	}
}
