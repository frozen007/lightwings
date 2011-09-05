/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-3-26
 *
 */
package org.zmy.db.proxy;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * PreparedStatementCallbackFilter
 *
 */
public class PreparedStatementCallbackFilter implements CallbackFilter {

    private PreparedStatement stdPs = null;

    private PreparedStatementInterceptor psi = null;

    private Map methodMap = new HashMap();

    private List callbacks = new ArrayList();

    private Class implClass = LogPreparedStatement.class;
    
    public PreparedStatementCallbackFilter(Class implClass, PreparedStatement ps) {
        stdPs = ps;
        init(implClass, new Class[] { PreparedStatement.class });
    }

    private void init(Class superclass, Class[] interfaces) {
        List methods = new ArrayList();
        Enhancer.getMethods(superclass, interfaces, methods);
        Map indexes = new HashMap();
        for (int i = 0, size = methods.size(); i < size; i++) {
            Method method = (Method) methods.get(i);
            Object callback = getCallback(method);
            if (callback == null)
                throw new IllegalStateException("getCallback cannot return null");
            boolean isCallback = callback instanceof Callback;
            if (!(isCallback || (callback instanceof Class)))
                throw new IllegalStateException("getCallback must return a Callback or a Class");
            if (i > 0 && ((callbacks.get(i - 1) instanceof Callback) ^ isCallback))
                throw new IllegalStateException(
                        "getCallback must return a Callback or a Class consistently for every Method");
            Integer index = (Integer) indexes.get(callback);
            if (index == null) {
                index = new Integer(callbacks.size());
                indexes.put(callback, index);
            }
            methodMap.put(method, index);
            callbacks.add(callback);
        }
    }

    protected Object getCallback(Method method) {
        Object callback = null;
        if (method.getDeclaringClass().equals(implClass)) {
            callback = NoOp.INSTANCE;
        } else {
            if (psi == null) {
                psi = new PreparedStatementInterceptor(stdPs);
            }
            callback = psi;
        }
        return callback;
    }

    public Callback[] getCallbacks() {
        if (callbacks.size() == 0)
            return new Callback[0];
        if (callbacks.get(0) instanceof Callback) {
            return (Callback[]) callbacks.toArray(new Callback[callbacks.size()]);
        } else {
            throw new IllegalStateException(
                    "getCallback returned classes, not callbacks; call getCallbackTypes instead");
        }
    }

    public Class[] getCallbackTypes() {
        if (callbacks.size() == 0)
            return new Class[0];
        if (callbacks.get(0) instanceof Callback) {
            return ReflectUtils.getClasses(getCallbacks());
        } else {
            return (Class[]) callbacks.toArray(new Class[callbacks.size()]);
        }
    }

    public int accept(Method method) {
        return ((Integer) methodMap.get(method)).intValue();
    }

    public int hashCode() {
        return methodMap.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof PreparedStatementCallbackFilter))
            return false;
        return methodMap.equals(((PreparedStatementCallbackFilter) o).methodMap);
    }
}
