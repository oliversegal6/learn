package threads.safety.shooting;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class InterateContainer {
	public static void main(String[] args) {
		Vector<Integer> list = new Vector<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
		
		new Thread(()->{
			synchronized (list) {
				Iterator<Integer> it = list.iterator();
				while (it.hasNext()) {
					Integer i = it.next();
					doSth(i);
				}
			}
		}).start();
		
		new Thread(()-> {
			list.add(1);
		}).start();
		
	}
	
	static void doSth(int i) {
		i += 10;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		i /= 3;
	}
}
