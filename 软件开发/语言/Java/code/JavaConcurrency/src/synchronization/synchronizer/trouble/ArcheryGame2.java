package threads.synchronization.synchronizer.trouble;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @case:
 * 10 players, 3 rounds
 * => For each round, after ALL players finished, we need to report their scores.
 * */
public class ArcheryGame2 {
	static int nPlayers = 10;
	static int nRound = 3;
	
	public static void main(String[] args) throws InterruptedException {
		
		Map<String, Integer> scores = new ConcurrentHashMap<>();
		ExecutorService pool = Executors.newFixedThreadPool(nPlayers);
		
		// synchronizer
		CountDownLatch[] gameCDs = new CountDownLatch[]{new CountDownLatch(nPlayers), new CountDownLatch(nPlayers), new CountDownLatch(nPlayers)};
		CountDownLatch[] reportCDs = new CountDownLatch[]{new CountDownLatch(1), new CountDownLatch(1), new CountDownLatch(1)};
		
		// game starts
		new Thread(()->{
			for (int j = 0; j < nRound; j++) {
				try {
					for (Player p : summonPlayers(nPlayers, gameCDs[j], scores)) {
						p.start();
					}
					
					reportCDs[j].await(10, TimeUnit.SECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		// game over, report scores:
		for (int i = 0; i < nRound; i++) {
			gameCDs[i].await(60, TimeUnit.SECONDS);
				System.out.println("Round " + (i+1) + " =======> Size: " + scores.size());
				scores.forEach((k,v)->{
					System.out.println("Score of " + k + ": " + v);
				});
			scores.clear();
			reportCDs[i].countDown();
		}
		
		pool.shutdown();
	}
	
	static Player[] summonPlayers(int nPlayers, CountDownLatch cd, Map<String, Integer> scores) {
		Player[] players = new Player[nPlayers];
		for (int i =0; i < nPlayers; i++) {
			Player p = new Player(cd, "player-"+ i, scores);
			players[i] = p;
		}
		return players;
	}
	
	/**
	 * represent 1 player who can play 1 round
	 * once finished, should call countdown.
	 * */
	static class Player extends Thread {
		
		private CountDownLatch cd;
		/** name, score */
		private Map<String, Integer> scores = new ConcurrentHashMap<>();
		
		public Player(CountDownLatch cd, String name, Map<String, Integer> scores) {
			setName(name);
			this.cd = cd;
			this.scores = scores;
		}

		@Override
		public void run() {
			try {
				int score = 50 + new Random().nextInt(50);
				Thread.sleep(100 * (score/100));
				scores.put(getName(), score);
				
				cd.countDown();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}

