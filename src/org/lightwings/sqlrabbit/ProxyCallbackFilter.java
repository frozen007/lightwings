package org.lightwings.sqlrabbit;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackHelper;

public class ProxyCallbackFilter extends CallbackHelper {

    public ProxyCallbackFilter(Class superclass, Class[] interfaces) {
        super(superclass, interfaces);
    }

    @Override
    protected Object getCallback(Method method) {
        return null;
    }

}
