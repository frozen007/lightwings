package org.lightwings.methodtimer;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AutoKnockServiceTransformer extends SimpleClassTransformer {

    public byte[] transformClass(String className, byte[] classfileBuffer) {
        if (!className.equals("com/stock/businesslogic/returnprocessing/AutoKnockService")) {
            return null;
        }
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        AutoKnockServiceClassVisitor visitor = new AutoKnockServiceClassVisitor(writer);
        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        return writer.toByteArray();
    }

    class AutoKnockServiceClassVisitor extends ClassAdapter {

        public AutoKnockServiceClassVisitor(ClassVisitor cv) {
            super(cv);
        }

        public MethodVisitor visitMethod(int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("processOrder")) {
                mv = new ProcessOrderMethodVisitor(mv);
            }
            return mv;
        }
    }

    class ProcessOrderMethodVisitor extends MethodAdapter implements Opcodes {

        public ProcessOrderMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitLdcInsn("DataPack Begin:");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(
                GETFIELD,
                "com/stock/businesslogic/returnprocessing/AutoKnockService",
                "datapack",
                "Lcom/stock/framework/communication/DataPack;");
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "com/stock/framework/communication/DataPack",
                "getSerialNum",
                "()J");
            mv.visitMethodInsn(
                INVOKESTATIC,
                "org/lightwings/methodtimer/ExecutionRecordLogger",
                "println",
                "(Ljava/lang/String;J)V");
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitLdcInsn("DataPack   End:");
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(
                    GETFIELD,
                    "com/stock/businesslogic/returnprocessing/AutoKnockService",
                    "datapack",
                    "Lcom/stock/framework/communication/DataPack;");
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "com/stock/framework/communication/DataPack",
                    "getSerialNum",
                    "()J");
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/methodtimer/ExecutionRecordLogger",
                    "println",
                    "(Ljava/lang/String;J)V");
            }
            mv.visitInsn(opcode);
        }

    }
}
