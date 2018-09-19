package threads.safety.shooting;

import java.text.SimpleDateFormat;

/**
 * Thread -> ThreadLocal.ThreadLocalMap threadLocals
 * */
public class DateFormat {
	
	static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};
	
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			new Thread(()->{
				try {
					System.out.println(sdf.get().parse("2017-12-29 10:34:13"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
