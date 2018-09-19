package threads.synchronization.patterns;

import java.io.FileWriter;
import java.io.Writer;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Balking {
	
	public static void main(String[] args) {
		MyFile file = new MyFile("src/threads/synchronization/patterns/balking.txt", "aaa");
		
		Runnable autoSaver = ()->{
			try {
				while (true) {
					file.save();
					Thread.sleep(2000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		Runnable changer = ()->{
			Random random = new Random();
			try {
				for (int i=0; true; i++) {
					file.edit("content-"+i);
					Thread.sleep(random.nextInt(2000));
					file.save();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		
		new Thread(autoSaver, "saver").start();
		new Thread(changer, "changer").start();
	}
}

class MyFile {
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private String filePath;
	private String content;
	private boolean isChanged;
	
	public MyFile(String filePath, String content) {
		this.filePath = filePath;
		this.content = content;
	}
	
	public void edit(String content) {
		lock.writeLock().tryLock();
			this.content = content;
			isChanged = true;
		lock.writeLock().unlock();
	}
	
	public void save() {
		lock.writeLock().tryLock();
		try {
			if (!isChanged) {
				return;
			} else {
				doSave();
				isChanged = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	private void doSave() {
		try {
			System.out.println(Thread.currentThread().getName() + " is saving content: " + content);
			Writer writer = new FileWriter(filePath);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}