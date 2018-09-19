package threads.task.executor.trouble;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CompleteOrder {
	
	static ExecutorService pool = Executors.newFixedThreadPool(4);
	static AtomicInteger counter = new AtomicInteger();
	
	public static void main(String[] args) {
		List<Future<Integer>> futures = new ArrayList<>();
		
		futures.add( pool.submit(()->{
			Thread.sleep(1000 * 5);
			System.out.println("task-1 completed ...");
			return 1;
		}));
		
		futures.add( pool.submit(()->{
			Thread.sleep(1000 * 1);
			System.out.println("task-2 completed ...");
			return 2;
		}));
		
		for (Future<Integer> f : futures) {
			try {
				System.out.println(f.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		pool.shutdown();
	}
}
