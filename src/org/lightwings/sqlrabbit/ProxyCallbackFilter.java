package org.lightwings.sqlrabbit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyCallbackFilter implements CallbackFilter {

    protected Object realObject = null;
    protected HashSet<Method> superMethods = new HashSet<Method>();
    private List<Callback> callbacks = new ArrayList<Callback>();
    private Map<Method, Integer> methodMap = new HashMap<Method, Integer>();
    protected Class superclass;
    protected Class[] interfaces;

    public ProxyCallbackFilter(Class superclass, Class[] interfaces, Object realObject) {
        this.superclass = superclass;
        this.realObject = realObject;
        Method[] methods = superclass.getDeclaredMethods();
        for (Method m : methods) {
            superMethods.add(m);
        }
    }

    public Callback[] getCallbacks() {
        List<Method> methods = new ArrayList<Method>();
        Enhancer.getMethods(superclass, interfaces, methods);
        Map<Callback, Integer> indexes = new HashMap<Callback, Integer>();
        for (int i = 0, size = methods.size(); i < size; i++) {
            Method method = (Method) methods.get(i);
            Callback callback = getCallback(method);
            if (callback == null)
                throw new IllegalStateException("getCallback cannot return null");
            Integer index = (Integer) indexes.get(callback);
            if (index == null) {
                index = new Integer(callbacks.size());
                indexes.put(callback, index);
            }
            methodMap.put(method, index);
            callbacks.add(callback);
        }

        return (Callback[]) callbacks.toArray(new Callback[callbacks.size()]);
    }

    protected Callback getCallback(Method method) {
        Callback callback = null;
        if (superMethods.contains(method)) {
            callback = new MethodInterceptor() {

                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            };
        } else {
            callback = new Dispatcher() {

                @Override
                public Object loadObject() throws Exception {
                    return realObject;
                }
            };
        }
        return callback;
    }

    public int accept(Method method) {
        return ((Integer) methodMap.get(method)).intValue();
    }

}
