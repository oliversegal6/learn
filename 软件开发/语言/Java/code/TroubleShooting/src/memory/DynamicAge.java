package memory;

/**
 *  -XX:+PrintGCDetails -Xmn45m -Xms90m -Xmx90m -XX:SurvivorRatio=1 -XX:+UseParNewGC
 *
 *  Fixed Heap: 90M
 *      - Survivor *2 : 15M *2
 *      - Eden: 15M
 *      - Tenured: 45M
 */
public class DynamicAge {

    public static void main(String[] args) throws InterruptedException {
        System.gc();

        byte[] b1 = new byte[1024 * 1024 * 4]; // Eden[b1]: 4/15, S0: 0/15, S1: 0/15, Tenured: 0/45
        byte[] b2 = new byte[1024 * 1024 * 4]; // Eden[b1,b2]: 8/15, S0: 0/15, S1: 0/15, Tenured: 0/45
        byte[] b3 = new byte[1024 * 1024 * 11]; // first minor gc, Eden[b3]: 11/15, S0[b1(age=1),b2(age=1)]: 8/15, S1: 0/15, Tenured: 0/45
        b3 = null;
        byte[] b4 = new byte[1024 * 1024 * 4]; // second minor gc, b3 should be collected, Eden[b4]: 4/15, S0: 0/15, S1: 0/15, Tenured[b1(age=2),b2(age=2)]: 8/45

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
    }
    
}
