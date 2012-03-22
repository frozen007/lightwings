package test.org.zmy.asm;

public class Foo {
    public Bar bar;

    public Foo(Bar bar){
        this.bar = bar;
    }
    public void doFoo(int i) {
        bar.doBar(i);
    }
}
