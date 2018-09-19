package threads.task.forkjoin;

import java.util.stream.IntStream;

public class CustomPool {
	public static void main(String[] args) {
		IntStream stream = IntStream.range(1, 10);
		
		stream.parallel().forEach(i->{
			try {
				Thread.sleep(2000);
				System.out.println(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		
		
	}
}
