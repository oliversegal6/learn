package threads.safety.visibility;

import java.text.ParseException;

public class MemoryInconsistency {
	
	static boolean isrunning = true;
	
	public void exec() {
		String tName = Thread.currentThread().getName();
		System.out.println(tName + " start");
		
		while (isrunning) {}
		
		System.out.println(tName + " end");
	}
	
	public static void main(String[] args) throws InterruptedException, ParseException {
		MemoryInconsistency m = new MemoryInconsistency();
		
		for (int i =0; i<5; i++) {
			new Thread(m::exec, "task-"+i).start();
		}
		
		Thread.sleep(1000);
		
		isrunning = false;
		
	}
}
