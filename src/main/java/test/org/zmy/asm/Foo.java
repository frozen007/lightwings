package test.org.zmy.asm;

public class Foo {
    public Bar bar;
    public Bar bar2;

    public Foo() {
        
    }

    public Foo(Bar bar, Bar bar2){
        this.bar = bar;
        this.bar2 = bar2;
    }
    public void doFoo(int i) {
        bar.doBar(i);
    }

    public void doFoo(char a) {
        System.out.println(a);
    }

    public long doFoo(int[] a) {
        if(bar!=null) {
            System.out.println("bar is not null");
        }
        System.out.println(a);
        long l=System.currentTimeMillis();
        return l;
    }
}
