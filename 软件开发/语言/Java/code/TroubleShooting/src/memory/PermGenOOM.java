package memory;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Run with JDK7
 * -XX:PermSize=20M -XX:MaxPermSize=20M
 */
public class PermGenOOM {

    public static void main(String[] args) {

        try {
            
            while(true){
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(Product.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object obj, Method method, Object[] args,
                                            MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(obj,args);
                    }
                });
                enhancer.create();
                Thread.sleep(50);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
    }
    
}

class Product {
    
}