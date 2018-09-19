package threads.task.executor.shooting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CompleteOrder {
	
	static ExecutorService pool = Executors.newFixedThreadPool(4);
	static CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(pool);
	
	static AtomicInteger counter = new AtomicInteger();
	
	public static void main(String[] args) {
		List<Future<Integer>> futures = new ArrayList<>();

		futures.add(cs.submit(()->{
			Thread.sleep(1000 * 5);
			System.out.println("task-1 completed ...");
			return 1;
		}));
		
		futures.add(cs.submit(()->{
			Thread.sleep(1000 * 1);
			System.out.println("task-2 completed ...");
			return 2;
		}));
		
		for (int i=0; i<2; i++) {
			try {
				System.out.println(cs.take().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		pool.shutdown();
	}
}
