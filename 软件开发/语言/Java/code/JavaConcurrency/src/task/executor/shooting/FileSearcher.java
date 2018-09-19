package threads.task.executor.shooting;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FileSearcher {
	
	static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

	public static void main(String[] args) {
		File root = new File("C:/Users/ZC45837/Desktop/Credit");
		
		long time = System.currentTimeMillis();
		
		search(root, ".exe");
		
		while (pool.getActiveCount() > 0 || pool.getQueue().size() > 0) {
			Thread.yield();
		}
		
		System.out.println("time: " + (System.currentTimeMillis() - time));
		
		pool.shutdown();
	}
	
	static void search(File file, String key) {
		if (file.isFile()) {
			if (file.getName().endsWith(key)) {
				System.out.println(file.getAbsolutePath());
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				pool.execute(()->{
					search(f, key);
				});
			}
		}
	}
}
