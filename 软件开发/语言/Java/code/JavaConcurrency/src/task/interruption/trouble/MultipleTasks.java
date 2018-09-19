package threads.task.interruption.trouble;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultipleTasks {
	public static void main(String[] args) throws InterruptedException {
		int nTask = 5;
		
		ExecutorService pool = Executors.newFixedThreadPool(nTask);

		for (int i = 0; i < nTask; i++) {
			MyTask t = new MyTask(i+1);
			t.setName("task-" + i);
			pool.execute(t);
		}
		
		pool.shutdown();
	}
	
	static class MyTask extends Thread {
		private int time;
		
		public MyTask(int time) {
			this.time = time;
		}

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(time * 1000);
					System.out.println(getName() + " is running ...");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("!!!!!!!! " + getName() + " has been interrupted");
				}
			}
		}
	}
}
