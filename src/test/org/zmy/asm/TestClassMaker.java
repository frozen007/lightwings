package test.org.zmy.asm;

import java.lang.reflect.Constructor;

import junit.framework.TestCase;

import org.lightwings.asm.ClassMaker;

public class TestClassMaker extends TestCase {

    public void test001() throws Exception {
        Class clazz = ClassMaker.makeClass(FooInterface.class, Hehe.class);
        Constructor con = clazz.getConstructor(Hehe.class);
        FooInterface fi = (FooInterface) con.newInstance(new Hehe());
        fi.doFoo(100);
    }

    public void test002() throws Exception {
        Class clazz = ClassMaker.makeClass(MakerTestInterface.class, MakerTest.class);
        Constructor con = clazz.getConstructor(MakerTest.class);
        MakerTestInterface test = (MakerTestInterface) con.newInstance(new MakerTest());
        test.test(100);
        test.test(10000l);
        test.test("hehe");
        test.test(new String[]{"hehe", "xixix"});
        test.test(new int[]{100, 1225});
        test.test();
    }
}
