package memory;

/**
 *  -XX:+PrintGCDetails -Xmn10m -Xms20m -Xmx20m -XX:SurvivorRatio=3 -XX:+UseParNewGC
 *  
 *  Fixed Heap: 20M
 *      - Survivor *2 : 2M *2
 *      - Eden: 6M
 *      - Tenured: 10M
 */
public class BiggerThanEdenObjToTenured {

    public static void main(String[] args) {
        
        byte[] b = new byte[1024 * 1024 * 8];

        boolean f = true;
        while(f) {
            Thread.yield();
        }
        System.out.println(b.toString());
        //  8MB memory should be taken from Tenured, as Eden is only 6MB
    }
    
}
