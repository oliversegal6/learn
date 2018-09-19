package threads.synchronization.locks.trouble;

public class NewObjectLock {
	
	public static void exec() {
		synchronized (new Object()) {
			try {
				String t = Thread.currentThread().getName();
				System.out.println(t + " start working");
				Thread.sleep(2000);
				System.out.println(t + " finish working");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Thread(NewObjectLock::exec).start();
		new Thread(NewObjectLock::exec).start();
	}
}
