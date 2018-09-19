package memory;

/**
 * 1. set linux "virtual memory" to 2G, default is unlimited
 *      command:  "ulimit -v 2097152"
 * 2. using default thread stack size (1024k in 64-bit vm), 
 *      command: java -Xms128m -Xmx128m -XX:MaxMetaspaceSize=128m memory.JavaVMStackOOM
 * 3. set thread stack size to 2m by -Xss2m or -XX:ThreadStackSize=2048
 *      command: java -Xss2m -Xms128m -Xmx128m -XX:MaxMetaspaceSize=128m memory.JavaVMStackOOM 
 */
public class JavaVMStackOOM {

    public static void main(String[] args) {

        long count = 0;
        while(true) {
            try {
                new Thread("thread-" + (count)) {
                    @Override
                    public void run() {
                        System.out.println(getName());
                        try {
                            Thread.sleep(Integer.MAX_VALUE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }.start();
                count++;
            } catch (Throwable e) {
                System.out.println("Created threads depth: " + count);
                // expected error: "java.lang.OutOfMemoryError: unable to create new native thread"
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
