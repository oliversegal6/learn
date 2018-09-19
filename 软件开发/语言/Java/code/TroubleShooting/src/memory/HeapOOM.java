package memory;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms20m -Xmx20m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        
        while (true) {
            list.add(new byte[1024 * 1024]);
        }
    }
    
}
