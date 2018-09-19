package threads.synchronization.locks.trouble;

public class StringLock {
	
	String LOCK = "LOCK";	
	static Object o = new Object();
	
	void work() {
		synchronized (new Object()) {
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
