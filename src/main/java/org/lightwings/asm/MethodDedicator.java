package org.lightwings.asm;

import org.objectweb.asm.MethodVisitor;

public interface MethodDedicator {

    public MethodVisitor getMethodVisitor(int access,
        String name,
        String desc,
        String signature,
        MethodVisitor mv);
}
