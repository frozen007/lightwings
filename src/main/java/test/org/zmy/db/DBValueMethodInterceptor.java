package test.org.zmy.db;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DBValueMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        
        
        //System.out.println(obj);
//        System.out.println("method="+method);
//        System.out.println("proxy="+proxy);
        return method.invoke(obj, args);
    }

}
