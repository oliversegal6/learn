package threads.synchronization.synchronizer.shooting;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncContainer<T> {
	LinkedList<T> list = new LinkedList<T>();
	private int count = 0;
	private int MAX = 10;
	
	Lock lock = new ReentrantLock();
	Condition canPut = lock.newCondition();
	Condition canGet = lock.newCondition();
	
	void put(T val) {
		lock.lock();
		try {
			while (list.size() == MAX) {
				canPut.await();
			}
			
			list.add(val);
			count++;
			
			canGet.signalAll(); // notify consumers
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	T get() {
		T t = null;
		try {
			if (list.size() == 0) {
				canGet.await();
			}
			
			t = list.removeFirst();
			count--;
			
			canPut.signalAll (); // notify producer
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
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
