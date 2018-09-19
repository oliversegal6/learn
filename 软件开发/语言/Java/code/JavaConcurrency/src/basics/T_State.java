package threads.basics;

import java.util.concurrent.TimeUnit;


/**
 *  TIME:	0			 1			2			3			4			5			6
 * 	main	new				
 *			&start 
 *			&sleeeeeeeep LooooooooooooooooooooK sleeeeeeeeep sleeeeeeeeeeeeeeeeeeeeeep
 *
 * 	t		started
 * 			&sleeeeeeeeeeeeeeeeeeeep			Lock
 * 												joooooooooooooooooooooooin
 * */
public class T_State {
	
	static Object lock = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(task());
		t.setName("task_A");
		state(0,t);
		
		t.start();
		state(1,t);
		
		synchronized (lock) {
			state(2,t);
		}
		
		state(1,t);
		state(2,t);
	}
	
	private static Runnable task() {
		return ()->{
			Thread t = Thread.currentThread();
			state(0,t);
			
			try {
				TimeUnit.SECONDS.sleep(2);
				
				synchronized (lock) {}
				
				Thread t2 = new Thread(()->{ 
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (Exception e) {
					e.printStackTrace();
				}});
				t2.start();
				
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	}
	
	private static void state(int time, Thread t) {
		try {
			TimeUnit.SECONDS.sleep(time);
			System.out.println(t.getState());	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
