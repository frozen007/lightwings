package org.lightwings.asm;

import org.objectweb.asm.MethodVisitor;

public interface MethodVisitorFactory {

    public MethodVisitor createMethodVisitor(MethodVisitor mv);
}
