package threads.task.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SearchTask {
	public static void main(String[] args) {
		
	}
	
	static class Search extends RecursiveTask<List<String>> {
		private static final long serialVersionUID = 1L;
		
		private File file;
		private String key;
		
		public Search(File file, String key) {
			this.file = file;
			this.key = key;
		}

		@Override
		protected List<String> compute() {
			List<String> names = new ArrayList<>();
			
			if (file.isFile()) {
				if (file.getName().endsWith(key)) {
					names.add(file.getAbsolutePath());
				}
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				Collection<Search> tasks = new ArrayList<>();
			}
			
			return names;
		}
		
	}
}
