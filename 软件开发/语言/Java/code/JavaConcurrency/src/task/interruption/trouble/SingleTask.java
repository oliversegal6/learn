package threads.task.interruption.trouble;

import java.util.concurrent.CountDownLatch;

public class SingleTask {
	public static void main(String[] args) throws InterruptedException {
		
		CountDownLatch cd = new CountDownLatch(1);
		
		Runnable task = () -> { 
			Thread t = Thread.currentThread();
			try {
				while (!t.isInterrupted()) {
					System.out.println("---> " + t.getName() + " is running ... ");
					Thread.sleep(10_000);
				}
			} catch (InterruptedException e) {
				System.out.println("---> Task has been interrupted --> " + e.getMessage());

				cd.countDown();
				System.out.println("---> task-interrupted ? " + t.isInterrupted());
			}
		};
		
		Thread t = new Thread(task);
		t.setName("task-1");
		t.start();
		
		Thread.sleep(2000);
		t.interrupt();
		
		cd.await();
		if (t.isInterrupted()) {
			System.out.println("Re-run task-1");
		} else {
			System.out.println("task-1 finished successfully, doing follow up works");
		}
	}
}
