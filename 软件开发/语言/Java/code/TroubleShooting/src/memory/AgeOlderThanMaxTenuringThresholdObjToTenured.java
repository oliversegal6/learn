package memory;

/**
 *  -XX:+PrintGCDetails -Xmn45m -Xms90m -Xmx90m -XX:SurvivorRatio=1 -XX:+UseParNewGC -XX:MaxTenuringThreshold=2
 *  
 *  Fixed Heap: 90M
 *      - Survivor *2 : 15M *2
 *      - Eden: 15M
 *      - Tenured: 45M
 */
public class AgeOlderThanMaxTenuringThresholdObjToTenured {

    public static void main(String[] args) throws InterruptedException {
        System.gc();
        
        byte[] b1 = new byte[1024 * 1024 * 2]; // Eden[b1]: 2/15, S0: 0/15, S1: 0/15, Tenured: 0/45
        byte[] b2 = new byte[1024 * 1024 * 2]; // Eden[b1,b2]: 4/15, S0: 0/15, S1: 0/15, Tenured: 0/45
        byte[] b3 = new byte[1024 * 1024 * 11]; // first minor gc, Eden[b3]: 11/15, S0[b1(age=1),b2(age=1)]: 4/15, S1: 0/15, Tenured: 0/45
        b3 = null;
        byte[] b4 = new byte[1024 * 1024 * 4]; // second minor gc, b3 should be collected, Eden[b4]: 4/15, S0: 0/15, S1[b1(age=2),b2(age=2)]: 4/15, Tenured: 0/45
        byte[] b5 = new byte[1024 * 1024 * 11]; // third minor gc, Eden[b5]: 11/15, S0[b4(age=1)]: 4/15, S1: 0/15, Tenured[b1,b2]: 4/45
        
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
        System.out.println(b5);
    }
    
}
