package memory;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeAllocation {

    public static void main(String[] args) {
        long size = 200000000;

        DirectIntArray dia = null;
        
        try {
            Thread.sleep(5000L);
            System.out.println("Start");
            
            dia = new DirectIntArray(size);
            for (int i = 0; i < size; i++) {
                dia.setValue(i, i);
                
                if (i < 10000) {
                    Thread.sleep(1);
                }
            }
            
            System.out.println("End");
            Thread.sleep(5000L);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (dia != null) {
                dia.destroy();
            }
        }
    }
    
}

class DirectIntArray {

    private final static long INT_SIZE_IN_BYTES = 4;

    private final long startIndex;

    Unsafe unsafe = null;

    public DirectIntArray(long size) throws Throwable {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        unsafe = (Unsafe) field.get(null);
        
        startIndex = unsafe.allocateMemory(size * INT_SIZE_IN_BYTES);
        unsafe.setMemory(startIndex, size * INT_SIZE_IN_BYTES, (byte) 0);
    }

    public void setValue(long index, int value) {
        unsafe.putInt(index(index), value);
    }

    public int getValue(long index) {
        return unsafe.getInt(index(index));
    }

    private long index(long offset) {
        return startIndex + offset * INT_SIZE_IN_BYTES;
    }

    public void destroy() {
        if (unsafe != null) {
            unsafe.freeMemory(startIndex);
        }
    }
}