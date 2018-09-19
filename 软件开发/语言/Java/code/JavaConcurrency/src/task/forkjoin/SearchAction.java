package threads.task.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;

public class SearchAction {
	public static void main(String[] args) {
		
		long time = System.currentTimeMillis();
		
		ForkJoinPool pool = ForkJoinPool.commonPool();
		
		File root = new File("C:/Users/ZC45837/Desktop/Credit");
		Search search = new Search(root, f->f.getName().endsWith(".exe"));
		//Search search = new Search(root, f->f.length() > 1024*1024 * 100); // 100m
		
		pool.execute(search);
		
		while (pool.getActiveThreadCount() > 0) {
			Thread.yield();
		}
		
		System.out.println("time used: " + (System.currentTimeMillis() - time));
	}
	
	static class Search extends RecursiveAction {

		private File file;
		private Predicate<File> condition;
		
		public Search(File file, Predicate<File> condition) {
			this.file = file;
			this.condition = condition;
		}

		@Override
		protected void compute() {
			if (file.isFile()) {
				if (condition.test(file)) {
					System.out.println(file.getAbsolutePath());
				}
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				Collection<Search> actions = new ArrayList<>();
				for (File f : files) {
					actions.add(new Search(f, condition));
				}
				invokeAll(actions);
 			}
		}
		
	} 
}