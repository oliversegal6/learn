package memory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Run with JDK8
 * -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
 */
public class MetaspaceOOM {

    public static void main(String[] args) {

        try {
            
            while(true){
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(A.class);
                enhancer.setUseCache(false);
                enhancer.setCallback((MethodInterceptor) (obj, method, args1, methodProxy) -> methodProxy.invokeSuper(obj, args1));
                enhancer.create();
                Thread.sleep(50);
        }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
    }
    
}

class A {
    
}