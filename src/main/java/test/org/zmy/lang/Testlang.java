package test.org.zmy.lang;

import junit.framework.TestCase;

public class Testlang extends TestCase {

    public void test001() {
        new SubBase(2);

    }

    public void test002() throws Exception {
        System.out.println(10>>>1);
        System.out.println(8>>2);
        System.out.println(1&~4);
        System.out.println(~1);

        double f = 1;
        double a = f/100;
        System.out.println(a);

        //System.out.println(Base.class);
        Class.forName("test.org.zmy.lang.Base");
        long time = 20120328152701l;
        System.out.println(time%1000000);
        
    }

}
