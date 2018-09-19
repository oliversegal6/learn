package threads.synchronization.synchronizer.shooting;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ArcheryGame2 {
	static int nPlayers = 10;
	static int nRound = 3;
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException, TimeoutException {
		Map<String, Integer> scores = new ConcurrentHashMap<>();
		ExecutorService pool = Executors.newFixedThreadPool(nPlayers);
		
		// synchronizer
		CyclicBarrier gameBarrier = new CyclicBarrier(nPlayers+1);
		CyclicBarrier scoreBarrier = new CyclicBarrier(2);
		
		// game starts
		new Thread(()->{
			for (int j = 0; j < nRound; j++) {
				try {
					for (Player p : summonPlayers(nPlayers, gameBarrier, scores)) {
						p.start();
					}
					
					scoreBarrier.await(10, TimeUnit.SECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		// game over, report scores:
		for (int i = 0; i < nRound; i++) {
			gameBarrier.await(10, TimeUnit.SECONDS);
				System.out.println("Round " + (i+1) + " =======> Size: " + scores.size());
				scores.forEach((k,v)->{
					System.out.println("Score of " + k + ": " + v);
				});
			scores.clear();
			scoreBarrier.await(10, TimeUnit.SECONDS);
			
			gameBarrier.reset();
			scoreBarrier.reset();
		}
		
		pool.shutdown();
	}
	
	static Player[] summonPlayers(int nPlayers, CyclicBarrier cb, Map<String, Integer> scores) {
		Player[] players = new Player[nPlayers];
		for (int i =0; i < nPlayers; i++) {
			Player p = new Player(cb, "player-"+ i, scores);
			players[i] = p;
		}
		return players;
	}
}

/**
 * represent 1 player who can play multiple round
 * once finished, need to await() other parties
 * */
class Player extends Thread {
	
	private CyclicBarrier cb;
	/** name, score */
	private Map<String, Integer> scores = new ConcurrentHashMap<>();
	
	public Player(CyclicBarrier cb, String name, Map<String, Integer> scores) {
		setName(name);
		this.cb = cb;
		this.scores = scores;
	}

	@Override
	public void run() {
		try {
			int score = 50 + new Random().nextInt(50);
			Thread.sleep(100 * (score/100));
			scores.put(getName(), score);
			
			cb.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
			System.out.println(e.getMessage());
		}
	}
}
