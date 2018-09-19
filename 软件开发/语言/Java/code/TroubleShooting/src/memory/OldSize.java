package memory;

/** 
 *  1. -Xmn10m -Xms30m -Xmx30m -XX:OldSize=10485760 -XX:-UseAdaptiveSizePolicy
 *      NewSize + OldSize = 10 + 10 < 30, then OldSize = [10, 30 - 10 = 20]
 *      
 *  2. -Xmn10m -Xms30m -Xmx30m -XX:OldSize=20971520 -XX:-UseAdaptiveSizePolicy
 *      NewSize + OldSize = 10 + 20 > 30, then OldSize = 30 - 10 = 20
 */
public class OldSize {

    public static void main(String[] args) {
        
        while(true) {
            Thread.yield();
        }
    }
    
}
