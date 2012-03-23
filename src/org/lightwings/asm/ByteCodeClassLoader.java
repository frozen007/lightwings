package org.lightwings.asm;

public class ByteCodeClassLoader extends ClassLoader {
    public Class load(String name, byte[] classbyte) {
        return super.defineClass(name, classbyte, 0, classbyte.length);
    }
}
