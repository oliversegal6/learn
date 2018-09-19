package threads.safety;

public class Reentrance {
	public static void main(String[] args) {
		new Thread(()->{
			new Child().doSomething();
		}).start();
		
		
		new Thread(()->{
			new Parent().doSomething();
		}).start();
	}
}

class Parent {
	synchronized void doSomething() {
		System.out.println("Parent do sth ...");
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	synchronized void elseThings() {
		System.out.println("Parent do some else ...");
	}
}

class Child extends Parent {
	@Override
	synchronized void doSomething() {
		System.out.println("Child do sth ...");
		super.doSomething();
	}
	
	synchronized void doSomeElse() {
		System.out.println("Child do some else ...");
	}
}