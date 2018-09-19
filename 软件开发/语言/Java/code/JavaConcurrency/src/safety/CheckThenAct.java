package threads.safety;

public class CheckThenAct {
	private static ExpensiveObject instance = null;
	
	public static void main(String[] args) {
		
	}
	
	public static ExpensiveObject getInstance() {
		if (instance == null) {
			instance = new ExpensiveObject();
		}
		return instance;
	}
	
}

class ExpensiveObject{}
