package org.lightwings.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodInjector extends ASMClassInnovator {

    protected MethodDedicator mvDedicator = null;

    public MethodInjector(MethodDedicator mvDedicator) {
        this.mvDedicator = mvDedicator;
    }

    @Override
    protected byte[] transformClass(String className, ClassReader reader, ClassWriter writer) {
        TargetClassVisitor cv = new TargetClassVisitor(writer);
        reader.accept(cv, ClassReader.SKIP_DEBUG);
        byte[] data = writer.toByteArray();
        return data;
    }

    class TargetClassVisitor extends ClassAdapter implements Opcodes {

        public TargetClassVisitor(ClassVisitor cv) {
            super(cv);
        }

        public MethodVisitor visitMethod(int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return mvDedicator.getMethodVisitor(access, name, desc, signature, mv);
        }
    }
}
