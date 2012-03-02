package test.org.zmy.lang;

public class Base {

    Base() {
        this(1);
        System.out.println("Base()");
    }

    public Base(int x) {
        System.out.println("Base(int x)");
    }
}
