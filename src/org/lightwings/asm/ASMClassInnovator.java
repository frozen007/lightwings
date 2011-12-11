package org.lightwings.asm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public abstract class ASMClassInnovator implements ClassInnovator {

    public byte[] innovate(String className) {
        if (!isInnovate(className)) {
            return null;
        }
        byte[] classBytes = getClassBytes(className);
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        return transformClass(className, reader, writer);
    }

    protected boolean isInnovate(String className) {
        return true;
    }

    protected abstract byte[] transformClass(String className, ClassReader reader, ClassWriter writer);

    public static byte[] getClassBytes(String className) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            InputStream is = ClassLoader.getSystemResourceAsStream(className + ".class");
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                bs.write(buf, 0, len);
            }
            bytes = bs.toByteArray();
        } catch (Exception e) {

        }
        return bytes;
    }

}
