package threads.synchronization.collection;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * delayed time == consumption time - production time
 * */
public class Delay {
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);
		DelayQueue<MyDelayed> dq = new DelayQueue<>();
		
		System.out.println(System.currentTimeMillis());
		MyDelayed d1 = new MyDelayed("aaa", 1 * 1000);
		MyDelayed d2 = new MyDelayed("bbb", 3 * 1000);
		MyDelayed d3 = new MyDelayed("ccc", 5 * 1000);
		
		dq.offer(d1);
		dq.offer(d2);
		dq.offer(d3);
		
		try {
			while (dq.size() > 0) {
				MyDelayed dd = dq.take();
				System.out.println(System.currentTimeMillis());
				System.out.println(dd.getData());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

class MyDelayed implements Delayed {
	private String data;
	/** initialization time + delay */
	private long startTime;
	
	public MyDelayed(String data, long delay) {
		this.data = data;
		this.startTime = System.currentTimeMillis() + delay;
	}

	@Override
	public int compareTo(Delayed o) {
		MyDelayed that = (MyDelayed)o;
		if (this.startTime < that.startTime) {
			return -1;
		} else if (this.startTime > that.startTime) {
			return 1;
		} 
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = startTime - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
