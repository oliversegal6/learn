package threads.safety.visibility;

import java.util.ArrayList;
import java.util.List;

public class VisibilityOnly {
	private volatile int count = 0;
	
	private void add() {
		for (int i = 0; i < 10_0000; i++) {
			count++;
		}
	}
	
	public static void main(String[] args) {
		VisibilityOnly v = new VisibilityOnly();
		
		List<Thread> tasks = new ArrayList<>();
		
		for (int i=0; i<10; i++) {
			tasks.add(new Thread(v::add, "task-"+i));
		}
		
		tasks.stream().forEach(t->t.start());
		
		tasks.stream().forEach(t->{
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		System.out.println(v.count);
	}
}
