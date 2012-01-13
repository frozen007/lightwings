package org.lightwings.sqlrabbit;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackHelper;

public class ProxyCallbackFilter extends CallbackHelper {
    
    protected Object realObject = null;

    public ProxyCallbackFilter(Class superclass, Class[] interfaces, Object realObject) {
        super(superclass, interfaces);
        this.realObject = realObject;
    }

    @Override
    protected Object getCallback(Method method) {
        return null;
    }

}
