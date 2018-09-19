	package threads.safety.atomicity.trouble;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CompositeAtomic {
	
	static AtomicInteger counter = new AtomicInteger(0);
	
	public void add() {
		for (int i=0;i<1000;i++) {
			if (counter.get() < 1000) {
				counter.incrementAndGet();
			}
		}
	}
	
	public static void main(String[] args) {
		CompositeAtomic c = new CompositeAtomic();
		List<Thread> tasks = new ArrayList<>();
		for (int i=0; i<10; i++) {
			tasks.add(new Thread(c::add));
		}
		
		tasks.forEach(t->t.start());
		tasks.forEach(t-> {
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		System.out.println(counter.get());
	}
}
