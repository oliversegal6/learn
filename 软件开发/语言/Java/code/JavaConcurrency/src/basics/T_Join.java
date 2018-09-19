package threads.basics;

import java.util.concurrent.TimeUnit;

public class T_Join {
	
	static int i = 0;
	
	public static void main(String[] args) throws InterruptedException {
		
		Runnable task = ()->{
			try {
				System.out.println("--> task start");
				i += 2;
				TimeUnit.SECONDS.sleep(2);
				System.out.println("--> task finish");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		System.out.println("main start: " + i);
		
			Thread t = new Thread(task);
			t.start();
			t.join();
		
		System.out.println("main finish: " + i);
		
	}
}
