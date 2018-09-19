package threads.task.interruption.trouble;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingleFutureTask {
	public static void main(String[] args) throws InterruptedException {
		
		Callable<String> task = () -> {
			Thread.sleep(60*1000);
			return "hehe~~";
		};
		
		Future<String> future = Executors.newSingleThreadExecutor().submit(task);
		new Thread(()->{
			try {
				String result = future.get();
				System.out.println(result);
			} catch (Exception e) {
				System.out.println("isDone ? " + future.isDone());
				System.out.println("isCancelled ? " + future.isCancelled());
			}
		}).start();
		
		Thread.sleep(1000);
		System.out.println("cancelling future task ...");
		// TODO:
		
	}
}
