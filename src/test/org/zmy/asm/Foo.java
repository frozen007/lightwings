package test.org.zmy.asm;

public class Foo {
    public Bar bar;

    public Foo() {
        
    }

    public Foo(Bar bar){
        this.bar = bar;
    }
    public void doFoo(int i) {
        bar.doBar(i);
    }

    public void doFoo(char a) {
        System.out.println(a);
    }

    public void doFoo(int[] a) {
        if(bar!=null) {
            System.out.println("bar is not null");
        }
        System.out.println(a);
        
    }
}
