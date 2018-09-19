package threads.safety;

import java.util.ArrayList;
import java.util.List;import java.util.concurrent.TimeUnit;


public class Read5 {
	
	List list = new ArrayList<>();
	
	public int size() {
		return list.size();
	}
	
	public void add(Object o) {
		list.add(o);
	}
	
	public static void main(String[] args) {
		Read5 r = new Read5();
		
		new Thread(()->{
			for (int i=0; i<10; i++) {
				r.add(new Object());
				System.out.println("add " + i);
				
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "t1").start();
		
		new Thread(()->{
			while (true) {// waste cpu
				if (r.size() == 5) {//not accurate
					break;
				}
			}
			System.out.println("t2 end");
		}, "t2").start();
	}
}
