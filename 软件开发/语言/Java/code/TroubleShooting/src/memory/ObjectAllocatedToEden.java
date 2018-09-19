package memory;

public class ObjectAllocatedToEden {

    public static void main(String[] args) throws InterruptedException {

        System.gc();
        Thread.sleep(10000L);
        
        byte[] obj = new byte[1024 * 1024];
        
        while (true) {
            Thread.yield();
        }
    }
    
}
