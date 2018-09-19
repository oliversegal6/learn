package memory;

/**
 * expected: java.lang.StackOverflowError
 */
public class JavaVMStackSOF {

    static long depth = 0;
    
    public static void main(String[] args) {
        try {
            increase();
        } catch (Throwable e) {
            System.out.println(e);
            System.out.println("depth = " + depth);
        }
    }
    
    
    public static void increase() {
        depth++;
        increase();
    }
}
