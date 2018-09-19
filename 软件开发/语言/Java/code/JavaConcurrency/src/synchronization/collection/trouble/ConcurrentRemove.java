package threads.synchronization.collection.trouble;

import java.util.Vector;
import java.util.stream.IntStream;

public class ConcurrentRemove {
	
	static Vector<Integer> list = new Vector<>();
	
	public static void main(String[] args) {
		
		IntStream.range(0, 10000).forEach(list::add);
		
		for (int i=0; i<10; i++) {
			new Thread(()->{
				while (list.size() > 0) { // check
					try {
						Thread.sleep(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// then act
					list.remove(0);
				}
			}).start();
		}
	}
}
