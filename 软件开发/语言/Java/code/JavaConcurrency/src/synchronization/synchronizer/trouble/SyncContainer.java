package threads.synchronization.synchronizer.trouble;

import java.util.LinkedList;

public class SyncContainer<T> {
	LinkedList<T> list = new LinkedList<T>();
	private int count = 0;
	private int MAX = 10;
	
	synchronized void put(T val) {
		if (list.size() == MAX) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		list.add(val);
		count++;
		this.notifyAll(); // notify consumers
	}
	
	synchronized T get() {
		T t = null;
		if (list.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		t = list.removeFirst();
		count--;
		this.notifyAll(); // notify producer
		return t;
	}
	
	public static void main(String[] args) {
		SyncContainer<Integer> s = new SyncContainer<>();
		
		for (int i=0; i<10; i++) {
			new Thread(()->{
				for (int j=0; j<10; j++) {
					System.out.println(Thread.currentThread().getName() + " get " + s.get());
				}
			}, "consumer-"+i).start();
		}
		
		for (int i=0; i<5; i++) {
			new Thread(() -> {
				for (int j=0; j<20; j++) {
					s.put(j);
					System.out.println(Thread.currentThread().getName() + " put " + j);
				}
			}, "producer-"+i).start();
		}
	}
}
