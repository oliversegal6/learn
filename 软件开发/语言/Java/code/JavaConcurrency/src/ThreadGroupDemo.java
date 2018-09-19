package threads;

/**
 * Full thread dump Java HotSpot(TM) Client VM (25.65-b01 mixed mode):

"Service Thread" #7 daemon prio=9 os_prio=0 tid=0x15092800 nid=0x2650 runnable [0x00000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread0" #6 daemon prio=9 os_prio=2 tid=0x1508c000 nid=0x504 waiting on condition [0x00000000]
   java.lang.Thread.State: RUNNABLE

"Attach Listener" #5 daemon prio=5 os_prio=2 tid=0x1509dc00 nid=0x24e0 waiting on condition [0x00000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 tid=0x1509b000 nid=0x21d4 runnable [0x00000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=1 tid=0x15058800 nid=0x2cb0 in Object.wait() [0x14f6f000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x04a06408> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
        - locked <0x04a06408> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=2 tid=0x15052800 nid=0x2d38 in Object.wait() [0x0119f000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x04a05f00> (a java.lang.ref.Reference$Lock)
        at java.lang.Object.wait(Object.java:502)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:157)
        - locked <0x04a05f00> (a java.lang.ref.Reference$Lock)

"main" #1 prio=5 os_prio=0 tid=0x0114e800 nid=0x114 runnable [0x00b3f000]
   java.lang.Thread.State: RUNNABLE
        at threads.ThreadGroupDemo.main(ThreadGroupDemo.java:16)

"VM Thread" os_prio=2 tid=0x1504dc00 nid=0x296c runnable

"VM Periodic Task Thread" os_prio=2 tid=0x150a7000 nid=0x2f78 waiting on condition

JNI global references: 5
 * */
public class ThreadGroupDemo {
	public static void main(String[] args) {
		ThreadGroup tg = new ThreadGroup("Group A");
		
		Runnable run = ()->{
			while(true) {
				
			}
		};
		
		Thread t1 = new Thread(tg,run,"aaa");
		Thread t2 = new Thread(tg,run,"bbb");
		Thread t3 = new Thread(tg,run,"ccc");
		
		
		t1.start();
		t2.start();
		t3.start();
		
		while(true) {}
	}
}
