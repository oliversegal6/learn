	package threads.safety.shooting;

import static java.util.stream.Collectors.toList;

import java.util.Vector;
import java.util.stream.IntStream;

public class GetLast {
	public static void main(String[] args) {
		Vector<Integer> list = new Vector<>(IntStream.range(0, 10).boxed().collect(toList()));
		new Thread(() ->{
			getLast(list);
		}).start();

		new Thread(()->{
			removeLast(list);
		}).start();
		
	}
	
	static Integer getLast(Vector<Integer> list) {
		int last = 0;
		try {
			int lastIndex = list.size() - 1;
			Thread.sleep(10);
			last = list.get(lastIndex);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return last;
	}
	
	static void removeLast(Vector<Integer> list) {
		int lastIndex = list.size() - 1;
		list.remove(lastIndex);
	}
	
}
