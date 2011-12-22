package test.org.zmy.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import junit.framework.TestCase;

public class TestDBValueProxy extends TestCase {
    DBValueImpl impl = new DBValueImpl();
    public void test001() {

        DBValue dbProxy =
            (DBValue) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{DBValue.class},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("method:" + method.getName());
                        return method.invoke(impl, args);
                        //return null;
                    }
                });
        dbProxy.execute();
    }

    public void test002() {
        DBValue proxy = (DBValue) Enhancer.create(DBValue.class, new Dispatcher(){

            @Override
            public Object loadObject() throws Exception {
                return new DBValueObject();
            }});
        proxy.execute();
    }

    public void test003() {
        Method[] ms = DBValueImpl.class.getDeclaredMethods();
        for (int m = 0; m < ms.length; m++) {
            Annotation[] as = ms[m].getAnnotations();
            for (int i = 0; i < as.length; i++) {
                System.out.println(as[i].toString());
            }
        }
    }
}
