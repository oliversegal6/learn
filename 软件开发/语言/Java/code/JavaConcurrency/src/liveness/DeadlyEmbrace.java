package threads.liveness;

public class DeadlyEmbrace {
	public static void main(String[] args) throws InterruptedException {
		Object lock1 = new Object();
		Object lock2 = new Object();
		
		Thread t1 = new Thread(()->{
			while(true) {
				synchronized (lock1) {
					synchronized (lock2) {
						System.out.println("t1");
					}
				}
			}
		});
		
		Thread t2 = new Thread(()->{
			while(true) {
				synchronized (lock2) {
					synchronized (lock1) {
						System.out.println("t2");
					}
				}
			}
		});
		
		t1.setName("task-1");
		t2.setName("task-2");
		t1.start();
		t2.start();
		
	}
}
