package threads.synchronization.synchronizer.shooting;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @case:
 * 10 players, 1 round
 * => After ALL player finished, we need to report their scores.
 * */
public class ArcheryGame {
	
	private static int nPlayer = 10;
	private static Map<Long, Integer> scores = new ConcurrentHashMap<>();
	
	public static void main(String[] args) throws InterruptedException {
		// synchronizer
		CountDownLatch cd = new CountDownLatch(nPlayer);
		
		// game starts
		for (int i=0; i < nPlayer; i++) {
			new Thread(()-> {
				try {
					int score = 50 + new Random().nextInt(50);
					Thread.sleep(100 * (score/100));
					scores.put(Thread.currentThread().getId(), score);
					
					cd.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		
		// game over, report scores:
		cd.await(60, TimeUnit.SECONDS);
		System.out.println(scores.size());
		scores.forEach((k,v)->{
			System.out.println("Score of thread " + k + ": " + v);
		});
	}
}
