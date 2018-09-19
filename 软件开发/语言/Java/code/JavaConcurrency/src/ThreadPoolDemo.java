package threads;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * time used: 2829
 * */
public class ThreadPoolDemo {

	private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
	
	public static void main(String[] args) {
		String root = "C:/Users/ZC45837/Desktop/Credit";

		long start = System.currentTimeMillis();
		search(new File(root), "sys");
		while (pool.getActiveCount() > 0) {
			Thread.yield();
		}
		System.out.println("time used: " + (System.currentTimeMillis() - start));
		pool.shutdown();
	}
	
	public static void search(File f, String keyword) {
		if (!f.isDirectory()) {
			if (f.getName().contains(keyword)) {
				System.out.println(f.getAbsolutePath());
			}
		} else {
			pool.submit(()->{
				File[] files = f.listFiles();
				for (File file : files) {
					search(file, keyword);
				}
			});
		}
	}

}
