package org.lightwings.asm;

import java.util.HashMap;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class ASMInfoClassVisitor extends ClassAdapter {
    private HashMap<String, ASMMethodInfo> asmMethodInfoMap = new HashMap<String, ASMMethodInfo>();

    public ASMInfoClassVisitor(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {
        ASMMethodInfo asmInfo = new ASMMethodInfo(access, name, desc, signature);
        asmMethodInfoMap.put(asmInfo.getKey(), asmInfo);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new AsmInfoMethodVisitor(asmInfo, mv);
        return mv;
    }

    public HashMap<String, ASMMethodInfo> getAsmMethodInfoMap() {
        return asmMethodInfoMap;
    }

    public void debugAsmInfoMap() {
        for (ASMMethodInfo asmInfo : asmMethodInfoMap.values()) {
            System.out.println(asmInfo.getKey()
                + ":MaxStack="
                + asmInfo.getMaxStack()
                + ",MaxLocals="
                + asmInfo.getMaxLocals());
        }
    }

    class AsmInfoMethodVisitor extends MethodAdapter {
        private ASMMethodInfo asmInfo = null;

        public AsmInfoMethodVisitor(ASMMethodInfo asmInfo, MethodVisitor mv) {
            super(mv);
            this.asmInfo = asmInfo;
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            asmInfo.setMaxStack(maxStack);
            asmInfo.setMaxLocals(maxLocals);
            mv.visitMaxs(maxStack, maxLocals);
        }

    }
}
