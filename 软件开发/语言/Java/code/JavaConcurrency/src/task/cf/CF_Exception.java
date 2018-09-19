package threads.task.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class CF_Exception {
	public static void main(String[] args) {
		
		/** tasks */
		Function<String, Integer> s2n = Integer::parseInt;
		Function<Integer, Double> circleArea = r -> r * 2 * Math.PI;
		
		/** 1. Create CompletableFuture */
		// if executor not provided, ForkJoin.commonPool() will be the default one.
		CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "1");
		
		
		/** 2. Transfer results */
		cf = cf.thenApply(s2n)
			   .thenApply(circleArea)
			   .thenApply(s-> "area is: " + s);
		
		
		
		// process the result, run tasks in different executor
		/*ExecutorService pool = Executors.newFixedThreadPool(4);
		cf.thenApplyAsync(s2n, pool).thenApplyAsync(circleArea, pool).thenApply(s-> "area is: " + s);*/
		
		try {
			System.out.println(cf.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
