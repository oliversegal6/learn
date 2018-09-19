package threads.synchronization.locks.trouble;
import java.util.concurrent.TimeUnit;


public class InterruptLongWait {
	
	synchronized public void task1() {
		try {
			System.out.println("task1 start");
			TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			System.out.println("task1 finished...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	synchronized public void task2() {
		try {
			System.out.println("task2 start");
			TimeUnit.SECONDS.sleep(1);
			System.out.println("task2 finished...");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		InterruptLongWait t = new InterruptLongWait();
		
		new Thread(t::task1, "task1").start();
		Thread task2 = new Thread(t::task2, "task2");
		task2.start();
		
		TimeUnit.SECONDS.sleep(2);
		System.out.println("trying to interrupt task2");
		task2.interrupt();
	}
}
