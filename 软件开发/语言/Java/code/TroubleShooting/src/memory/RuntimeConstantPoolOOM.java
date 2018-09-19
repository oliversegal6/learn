package memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. Run with JDK6
 *      -XX:PermSize=10M -XX:MaxPermSize=10M
 *      Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
 * 2. Run with JDK7
 *      -XX:InitialHeapSize=10M -XX:MaxHeapSize=10M -XX:MaxTenuringThreshold=0 -XX:SurvivorRatio=1 -XX:+UseParNewGC
 *      Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 */     
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
            if (i < 1000) {
                Thread.sleep(10);
            }
        }
    }
    
}
