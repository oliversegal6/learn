package threads.synchronization.synchronizer.trouble;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;


/**
 * @case:
 * 3 rounds
 * - 1st round: 10 players
 * - 2nd round: 4 players
 * - 3rd round: 2 players
 * ==> for each round, we need to report their scores
 * */
public class ArcheryGame3 {
	static int nRound = 3;
	static int[] nPlayers = new int[] {10, 6, 2};
	static Map<String, Integer> scores = new ConcurrentHashMap<>();
	
	public static void main(String[] args) throws InterruptedException {
		Phaser gamePhaser = new Phaser();
		Phaser scorePhaser = new Phaser();

		gamePhaser.register();
		scorePhaser.register();
		
		Player[] players = summonPlayers(nPlayers[0], gamePhaser, scorePhaser, scores);
		for (Player p : players) {
			p.start();
		}
		
		for (int i = 0; i < nRound; i++) {
			gamePhaser.arriveAndAwaitAdvance();
			
			System.out.println("Round " + (i+1) + " ========> ");
			reportScores();
			
			if (i < nRound-1) {
				Arrays.stream(players)
					.filter(p -> p.isAlive)
					.sorted((p1, p2) -> p1.getScore() - p2.getScore())
					.limit(nPlayers[i+1])
					.forEach(p -> p.setAlive(false));
			
				scorePhaser.arriveAndAwaitAdvance();
			}
		}

		Arrays.stream(players).forEach(p->p.setAlive(false));
		gamePhaser.arriveAndDeregister();
		scorePhaser.arriveAndDeregister();
	}
	
	static void reportScores() {
		scores.forEach((k,v)->{
			System.out.println("Score of " + k + ": " + v);
		});
		scores.clear();
	}
	
	static Player[] summonPlayers(int nPlayers, Phaser gp, Phaser sp, Map<String, Integer> scores) {
		Player[] players = new Player[nPlayers];
		for (int i =0; i < nPlayers; i++) {
			Player p = new Player(gp, sp, "player-"+i, scores);
			players[i] = p;
		}
		return players;
	}
	
	
	/**
	* represent 1 player who can play 1 round
	* once finished, should call countdown.
	* */
	static class Player extends Thread {
		private Phaser gp;
		private Phaser sp;
		private int score = 0;
		private volatile boolean isAlive = true;
		
		/** name, score */
		private Map<String, Integer> scores = new ConcurrentHashMap<>();
	
		public Player(Phaser gp, Phaser sp, String name, Map<String, Integer> scores) {
			setName(name);
			this.gp = gp;
			this.sp = sp;
			this.scores = scores;
			
			gp.register();
			sp.register();
		}
	
		@Override
		public void run() {
			while (isAlive) {
				try {
					score = 50 + new Random().nextInt(50);
					Thread.sleep(100 * (score/100));
					scores.put(getName(), score);
					
					gp.arriveAndAwaitAdvance();
					
					sp.arriveAndAwaitAdvance();
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
			gp.arriveAndDeregister();
			sp.arriveAndDeregister();
		}
		
		public void setAlive(boolean isAlive) {
			this.isAlive = isAlive;
		}
		public int getScore() {
			return score;
		}
	}
	
}
