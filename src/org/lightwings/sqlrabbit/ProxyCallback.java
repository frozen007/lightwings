package org.lightwings.sqlrabbit;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyCallback implements MethodInterceptor {
    
    protected Object realObject = null;
    protected Class proxyClass = null;
    
    public ProxyCallback(String realObject, Class proxyClass) {
        this.realObject = realObject;
        this.proxyClass = proxyClass;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        
        return null;
    }

}
