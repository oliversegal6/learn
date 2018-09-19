package memory;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xmx50m -Xms50m -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=30 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintReferenceGC -XX:+UnlockDiagnosticVMOptions -XX:+G1PrintRegionLivenessInfo -XX:+PrintAdaptiveSizePolicy
 */
public class G1 {

    public static void main(String[] args) throws InterruptedException {
//        Thread.sleep(15000L);


        byte[] o = new byte[1024 * 1024 * 10];
        System.out.println("first humongous object created: " + o);

        List<byte[]> list = new ArrayList<>();

        System.out.println("start");
        for (int i = 0; i < 100; i++) {
            if (i == 10) {
                o = null;
                System.out.println("create second humongous object");
                o = new byte[1024 * 1024 * 20];
                System.out.println("second humongous object created: " + o);
            }
            list.add(new byte[1024 * 1024 / 4]);
            Thread.sleep(100L);
        }

        System.out.println("done");

        while (true) {
            Thread.yield();
        }
    }

}
