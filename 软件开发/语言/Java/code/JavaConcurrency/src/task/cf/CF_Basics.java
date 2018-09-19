package threads.task.cf;

import java.util.concurrent.CompletableFuture;

public class CF_Basics {
	
	public static void main(String[] args) {
		String result = "";
		
		result = transfer();		System.out.println("transfer: " + result);
		consume();
		run();
		System.out.println("====================");
		
		result = transfer2();		System.out.println("transfer2: " + result);
		consume2();
		run2();
		System.out.println("====================");
		
		result = transferAny();		System.out.println("transferAny: " + result);
		consumeAny();
		runAfterAny();
	}
	
	public static String transfer() {
		return CompletableFuture.supplyAsync(()->"hello").thenApply(s->s.toUpperCase()).join();
	}
	
	public static void consume() {
		CompletableFuture.supplyAsync(()->"hello").thenAccept(s->System.out.println("consume: " + s)).join();
	}
	
	public static void run() {
		CompletableFuture.supplyAsync(()->"aaa").thenRun(()->{System.out.println("run: run something");}).join();
	}
	
	public static String transfer2() {
		return CompletableFuture.supplyAsync(()->"hello").thenCombine(CompletableFuture.supplyAsync(()->"world"), (a,b)-> (a+" "+b).toUpperCase()).join();
	}
	
	public static void consume2() {
		CompletableFuture.supplyAsync(()->"hello").thenAcceptBoth(CompletableFuture.supplyAsync(()->"world"), (a,b)-> System.out.println("consume2: " + a+" "+b));
	}
	
	public static void run2() {
		CompletableFuture.supplyAsync(()->"aaa").runAfterBoth(CompletableFuture.supplyAsync(()->"bbb"), ()-> System.out.println("run2: run something after both"));
	}
	
	public static String transferAny() {
		return CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_a";
		}).applyToEither(CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_b";
		}), s->s.toUpperCase()).join();
	}
	
	public static void consumeAny() {
		CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_a";
		}).acceptEither(CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_b";
		}), s->System.out.println("consumeAny: "+s.toUpperCase()));
	}
	
	public static void runAfterAny() {
		CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_a";
		}).runAfterEither(CompletableFuture.supplyAsync(()-> {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "task_b";
		}), ()->System.out.println("runAfterAny: run something after any task finished.")).join();
	}
}
