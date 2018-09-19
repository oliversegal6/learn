package memory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * -XX:MaxDirectMemorySize=10M
 */
public class ByteBufferAllocationOOM {

    public static void main(String[] args) {

        int count = 0;

        List<ByteBuffer> list = new ArrayList<>();
        while (true) {

            count++;
            list.add(ByteBuffer.allocateDirect(1024 * 1024));

            System.out.println(count);

        }
    }

}
