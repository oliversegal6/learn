package threads.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CallableTask {
	public static void main(String[] args) {
		Future<Integer> future = Executors.newSingleThreadExecutor()
				.submit(()->{
					Thread.sleep(200);
					return 1;
				});
		long time = System.currentTimeMillis();
		
		try {
			System.out.println(future.get());
			System.out.println("time: " + (System.currentTimeMillis() - time));
			int a = future.get();
			System.out.println(a);
			
			System.out.println(future.isDone());
			System.out.println(future.isCancelled());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
