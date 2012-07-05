package org.lightwings.sqlrabbit;

import net.sf.cglib.proxy.Enhancer;

public class ProxyEnhancer {

    public static Object create(Class superclass, Class[] interfaces, Object realObject) {
        ProxyCallbackFilter callbackfilter = new ProxyCallbackFilter(superclass, interfaces, realObject);
        Object proxyObject = Enhancer.create(superclass, interfaces, callbackfilter, callbackfilter.getCallbacks());
        return proxyObject;
    }
}
