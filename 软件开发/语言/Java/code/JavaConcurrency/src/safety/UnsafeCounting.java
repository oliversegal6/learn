package threads.safety;

public class UnsafeCounting {
	
	private static long count;

	static void add() {
		++count;
	}
	
	public static void main(String[] args) {
		System.out.println(count);
	}
}
