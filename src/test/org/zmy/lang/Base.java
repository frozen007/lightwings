package test.org.zmy.lang;

public class Base {
    static {
        System.out.println("static Base");
    }

    public static void staticBae() {
        
    }

    Base() {
        this(1);
        System.out.println("Base()");
    }

    public Base(int x) {
        System.out.println("Base(int x)");
    }
}
