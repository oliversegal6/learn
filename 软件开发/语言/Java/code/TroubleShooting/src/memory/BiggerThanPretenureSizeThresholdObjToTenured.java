package memory;

/**
 *  -XX:+PrintGCDetails -Xmn16m -Xms30m -Xmx30m -XX:SurvivorRatio=2 -XX:+UseParNewGC -XX:PretenureSizeThreshold=3145728 -XX:-UseTLAB
 *  
 *  Fixed Heap: 30M
 *      - Survivor *2 : 4M *2
 *      - Eden: 8M
 *      - Tenured: 14M
 */
public class BiggerThanPretenureSizeThresholdObjToTenured {

    public static void main(String[] args) throws InterruptedException {
        
        System.gc();
        Thread.sleep(10000L);
        
        byte[] b = new byte[1024 * 1024 * 3 + 1];

        boolean f = true;
        while(f) {
            Thread.yield();
        }
        System.out.println(b.toString());
        //  3MB memory should be taken from Tenured
    }
    
}
