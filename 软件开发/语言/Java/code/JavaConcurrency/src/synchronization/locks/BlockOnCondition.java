package threads.synchronization.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockOnCondition {
	
	private Lock lock = new ReentrantLock();
	private Condition meetingRoomEmpty = lock.newCondition();
	private boolean isEmpty = true;
	
	public static void main(String[] args) throws InterruptedException {
		BlockOnCondition boc = new BlockOnCondition();
		
		new Thread(boc::haveMeeting, "team-A").start();
		new Thread(boc::cleanMeetingRoom, "AYi").start();
		new Thread(boc::haveMeeting, "team-B").start();
		
	}
	
	void haveMeeting() {
		lock.lock();
		
		try {
			while (!isEmpty) {
				meetingRoomEmpty.await();
			}
			
			if (isEmpty) {
				isEmpty = false;
				System.out.println(Thread.currentThread().getName() + " start meeting ....");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(Thread.currentThread().getName() + " finish meeting ....");
				isEmpty = true;
			}
		
			meetingRoomEmpty.signal();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	void cleanMeetingRoom() {
		lock.lock();
		
		try {
			while (!isEmpty) {
				meetingRoomEmpty.await();
			}
			
			if (isEmpty) {
				isEmpty = false;
				System.out.println(Thread.currentThread().getName() + " start cleaning meeting room ....");
				TimeUnit.SECONDS.sleep(1);
				System.out.println(Thread.currentThread().getName() + " finish cleaning meeting room ....");
				isEmpty = true;
			}
		
			meetingRoomEmpty.signal();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
}
