package threads.task.cf;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CF_Listen {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		List<String> list = Arrays.asList("A", "B", "C", "D");
		
		list.stream().map(x -> CompletableFuture.supplyAsync(()-> x+x))
					 .map(f -> f.whenComplete((result, error) -> System.out.println(result + " Error:" + error)))
					 .count();
		
	}
}
