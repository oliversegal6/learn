package threads.safety.trouble;

/**
 * jstack: detect deadlock
 * */
public class DeadLock {
	
	private static Object lock1 = new Object();
	private static Object lock2 = new Object();
	
	public static void main(String[] args) {
		new Thread(()-> a()).start();
		new Thread(()-> b()).start();
	}
	
	public static void a() {
		while (true) {
			synchronized (lock1) {
				synchronized (lock2) {
					System.out.println("aaa");
				}
			}
		}
	}
	
	public static void b() {
		while (true) {
			synchronized (lock2) {
				synchronized (lock1) {
					System.out.println("bbb");
				}
			}
		}
	}
	
}
