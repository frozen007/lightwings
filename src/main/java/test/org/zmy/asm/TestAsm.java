package test.org.zmy.asm;

import org.objectweb.asm.commons.Method;

import junit.framework.TestCase;

public class TestAsm extends TestCase {

    public void test001() {
        Class clazz = FooInterface.class;
        java.lang.reflect.Method[] ms = clazz.getMethods();
        for (int i = 0; i < ms.length; i++) {
            Method m = Method.getMethod(ms[i]);
            System.out.println(m);
        }
    }
}
