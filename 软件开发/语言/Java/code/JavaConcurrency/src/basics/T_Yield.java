package threads.basics;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class T_Yield {
	public static void main(String[] args) throws InterruptedException, IOException {
		
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		Runnable task = ()->{
			try {
				for (int i=0;i<10;i++) {
					System.out.println("task-1 ...");
					Thread.yield();
				}
				
				while (true) {
					Thread.yield();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		};
		
		Runnable task2 = ()->{
			try {
				for (int i=0;i<10;i++) {
					System.out.println("task-2 ...");
					Thread.yield();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		};
		
		pool.execute(task);
		pool.execute(task2);
		
		pool.shutdown();
	}
	
}
