package org.lightwings.methodtimer;

import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExecutionRecordClassVisitor extends ClassAdapter {
    private HashMap<String, MethodAsmInfo> asmInfoMap = null;
    private HashSet<String> methodSet = null;
    private String className = null;
    private String shortClassName = null;

    public ExecutionRecordClassVisitor(HashMap<String, MethodAsmInfo> asmInfoMap, ClassVisitor cv) {
        super(cv);
        this.asmInfoMap = asmInfoMap;
    }

    public void setMethodSet(HashSet<String> methodSet) {
        this.methodSet = methodSet;
    }

    public void visit(final int version,
        final int access,
        final String name,
        final String signature,
        final String superName,
        final String[] interfaces) {
        className = name;
        shortClassName = className.substring(className.lastIndexOf("/") + 1);
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodAsmInfo asmInfo = asmInfoMap.get(MethodAsmInfo.genKey(access, name, desc, signature));
        if (methodSet == null || methodSet.isEmpty() || methodSet.contains(asmInfo.getKey())) {
            System.out.println("method " + name + " ripped");
            mv = new ExecutionRecordMethodVisitor(asmInfo, mv);
        }
        return mv;
    }

    class ExecutionRecordMethodVisitor extends MethodAdapter implements Opcodes {
        private MethodAsmInfo asmInfo = null;
        private int beginTimeSlotIndex;
        private int endTimeSlotIndex;

        public ExecutionRecordMethodVisitor(MethodAsmInfo asmInfo, MethodVisitor mv) {
            super(mv);
            this.asmInfo = asmInfo;
            int maxLocals = this.asmInfo.getMaxLocals();
            beginTimeSlotIndex = maxLocals + 1;
            endTimeSlotIndex = beginTimeSlotIndex + 2;
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
            mv.visitVarInsn(LSTORE, beginTimeSlotIndex);
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitLdcInsn(shortClassName + "." + asmInfo.getShortKey() + ":");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
                mv.visitVarInsn(LSTORE, endTimeSlotIndex);
                mv.visitVarInsn(LLOAD, endTimeSlotIndex);
                mv.visitVarInsn(LLOAD, beginTimeSlotIndex);
                mv.visitInsn(LSUB);
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/methodtimer/ExecutionRecordLogger",
                    "println",
                    "(Ljava/lang/String;J)V");
            }
            mv.visitInsn(opcode);
        }
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(maxStack, maxLocals + 4);
        }

    }
}
