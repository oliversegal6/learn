package threads.task.cf;

import java.util.concurrent.CompletableFuture;

public class CF_Consume {
	public static void main(String[] args) {
		CompletableFuture.supplyAsync(()->{ return "hello"; }).thenAccept(s-> System.out.println(s + " world~~")).join();
	}
}
