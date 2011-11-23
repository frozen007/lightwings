package org.lightwings.methodtimer;

import java.util.HashMap;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class AsmInfoClassVisitor extends ClassAdapter {
    private HashMap<String, MethodAsmInfo> asmInfoMap = new HashMap<String, MethodAsmInfo>();

    public AsmInfoClassVisitor(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {
        MethodAsmInfo asmInfo = new MethodAsmInfo(access, name, desc, signature);
        asmInfoMap.put(asmInfo.getKey(), asmInfo);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new AsmInfoMethodVisitor(asmInfo, mv);
        return mv;
    }

    public HashMap<String, MethodAsmInfo> getAsmInfoMap() {
        return asmInfoMap;
    }

    public void debugAsmInfoMap() {
        for (MethodAsmInfo asmInfo : asmInfoMap.values()) {
            System.out.println(asmInfo.getKey()
                + ":MaxStack="
                + asmInfo.getMaxStack()
                + ",MaxLocals="
                + asmInfo.getMaxLocals());
        }
    }

    class AsmInfoMethodVisitor extends MethodAdapter {
        private MethodAsmInfo asmInfo = null;

        public AsmInfoMethodVisitor(MethodAsmInfo asmInfo, MethodVisitor mv) {
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
