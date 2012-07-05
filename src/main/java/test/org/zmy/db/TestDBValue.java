package test.org.zmy.db;

import net.sf.cglib.proxy.Enhancer;
import junit.framework.TestCase;

public class TestDBValue extends TestCase {

    public void test001() {
        DBValue dbimpl = new DBValueImpl();
        Object proxy = Enhancer.create(DBValue.class, new DBValueMethodInterceptor());
        DBValue dbProxy = (DBValue) proxy;
        //dbProxy.setDBValue(dbimpl);
        dbProxy.execute();
    }
}
