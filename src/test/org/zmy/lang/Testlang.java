package test.org.zmy.lang;

import junit.framework.TestCase;

public class Testlang extends TestCase {

    public void test001() {
        new SubBase(2);

    }

    public void test002() {
        System.out.println(2>>>1);
        System.out.println(8>>2);
        System.out.println(1&~4);
        double f = 1;
        double a = f/100;
        System.out.println(a);
    }
}
