package memory;

import java.nio.ByteBuffer;

public class ByteBufferAllocation {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer);
        
        buffer.putChar('a');
        System.out.println(buffer);
        buffer.putChar('c');
        System.out.println(buffer);
        buffer.putInt(10);
        System.out.println(buffer);
    }
    
}
