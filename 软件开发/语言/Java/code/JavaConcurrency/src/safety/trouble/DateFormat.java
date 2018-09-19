package threads.safety.trouble;

import java.text.SimpleDateFormat;

public class DateFormat {
	
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			new Thread(()->{
				try {
					System.out.println(sdf.parse("2017-12-29 10:34:13"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
