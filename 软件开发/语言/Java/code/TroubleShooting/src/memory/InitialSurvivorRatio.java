package memory;

/**
 * 1. -XX:InitialSurvivorRatio not set
 *      1.1 -XX:SurvivorRatio not set
 *          -XX:+UseParallelGC -XX:+PrintGCDetails -Xmn10m -Xms20m -Xmx20m
 *          
 *          Eden: 8MB
 *          Survivor: 1MB
 *          
 *      1.2 -XX:SurvivorRatio is explicitly set
 *          -XX:+UseParallelGC -XX:+PrintGCDetails -Xmn14m -Xms20m -Xmx20m -XX:SurvivorRatio=10
 *      
 *          Eden: 12MB
 *          Survivor: 1MB
 * 
 * 2. -XX:InitialSurvivorRatio is explicitly set
 *      -XX:+UseParallelGC -XX:+PrintGCDetails -Xmn12m -Xms20m -Xmx20m -XX:InitialSurvivorRatio=10
 *      
 *          Eden: 10MB
 *          Survivor: 1MB
 */
public class InitialSurvivorRatio {

    public static void main(String[] args) {
        
        while(true) {
            Thread.yield();
        }
        
    }
    
}
