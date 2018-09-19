package threads.task.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Pool {
	public static void main(String[] args) throws InterruptedException {
		
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, 
														new ArrayBlockingQueue<Runnable>(10), 
														(worker, executor)->{
															System.out.println(worker.toString() + " is rejected");
														});
		
		Runnable task = () -> {
			try {
				TimeUnit.SECONDS.sleep(60*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		for (int i = 0; i<21; i++) {
			pool.execute(task);
			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println("total pool size: " + pool.getPoolSize() + ", task queue: " + pool.getQueue().size());
		}
		
		System.out.println("all tasks submitted...");
		
	}
}
