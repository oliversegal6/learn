package threads.synchronization.collection.trouble;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashMap_Put {
	public static void main(String[] args) {
	
		Map<Integer, Integer> map = new HashMap<>();
		Random random = new Random();
		
		for (int i=0; i<200; i++) {
			new Thread(()->{
				for (int j=0; j<100_000; j++) {
					map.put(random.nextInt(10000), j+random.nextInt(100));
				} 
			},"t-"+i).start();
		}
		
	}
	
}
