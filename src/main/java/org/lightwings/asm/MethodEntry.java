package org.lightwings.asm;

public class MethodEntry {
    protected ASMMethodInfo methodInfo = null;
    protected MethodVisitorFactory mvf = null;

    public MethodEntry(ASMMethodInfo methodInfo, MethodVisitorFactory mvf) {
        this.methodInfo = methodInfo;
        this.mvf = mvf;
    }

    public ASMMethodInfo getMethodInfo() {
        return this.methodInfo;
    }

    public MethodVisitorFactory getMethodVisitor() {
        return this.mvf;
    }
}
