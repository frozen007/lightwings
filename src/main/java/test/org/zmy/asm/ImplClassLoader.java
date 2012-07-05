package test.org.zmy.asm;

public class ImplClassLoader extends ClassLoader {

    public Class load(String name, byte[] classbyte) {
        return super.defineClass(name, classbyte, 0, classbyte.length);
    }
}
