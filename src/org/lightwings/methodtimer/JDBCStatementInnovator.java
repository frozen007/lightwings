package org.lightwings.methodtimer;

import org.lightwings.asm.ASMClassInnovator;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JDBCStatementInnovator extends ASMClassInnovator {
    private String transformedClass = null;

    public JDBCStatementInnovator(String className) {
        transformedClass = className;
    }

    @Override
    protected byte[] transformClass(String className, ClassReader reader, ClassWriter writer) {
        OracleStatementClassVisitor visitor = new OracleStatementClassVisitor(writer);
        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        System.out.println("jdbc:" + className);
        return writer.toByteArray();
    }

    class OracleStatementClassVisitor extends ClassAdapter {

        public OracleStatementClassVisitor(ClassVisitor cv) {
            super(cv);
        }

        public MethodVisitor visitMethod(int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("executeUpdate") && desc.equals("()I")) {
                mv = new ExecuteUpdateMethodVisitor(mv);
            } else if (name.equals("executeBatch") && desc.equals("()[I")) {
                mv = new ExecuteBatchMethodVisitor(mv);
            } else if(name.equals("addBatch") && desc.equals("()V")) {
                mv = new AddBatchMethodVisitor(mv);
            }

            return mv;
        }

        class ExecuteUpdateMethodVisitor extends MethodAdapter implements Opcodes {

            public ExecuteUpdateMethodVisitor(MethodVisitor mv) {
                super(mv);
            }

            @Override
            public void visitCode() {
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    transformedClass,
                    "getOriginalSql",
                    "()Ljava/lang/String;");
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/methodtimer/SQLIntercepter",
                    "isIntercept",
                    "(Ljava/lang/String;)Z");
                Label l0 = new Label();
                mv.visitJumpInsn(IFEQ, l0);
                mv.visitInsn(ICONST_1);
                mv.visitInsn(IRETURN);
                mv.visitLabel(l0);
            }
        }

        class ExecuteBatchMethodVisitor extends MethodAdapter implements Opcodes {

            public ExecuteBatchMethodVisitor(MethodVisitor mv) {
                super(mv);
            }

            @Override
            public void visitCode() {
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    transformedClass,
                    "getOriginalSql",
                    "()Ljava/lang/String;");
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/methodtimer/SQLIntercepter",
                    "isIntercept",
                    "(Ljava/lang/String;)Z");
                Label l0 = new Label();
                mv.visitJumpInsn(IFEQ, l0);
                mv.visitInsn(ACONST_NULL);
                mv.visitInsn(ARETURN);
                mv.visitLabel(l0);
            }
        }

        class AddBatchMethodVisitor extends MethodAdapter implements Opcodes {

            public AddBatchMethodVisitor(MethodVisitor mv) {
                super(mv);
            }

            @Override
            public void visitCode() {
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    transformedClass,
                    "getOriginalSql",
                    "()Ljava/lang/String;");
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/methodtimer/SQLIntercepter",
                    "isIntercept",
                    "(Ljava/lang/String;)Z");
                Label l0 = new Label();
                mv.visitJumpInsn(IFEQ, l0);
                mv.visitInsn(RETURN);
                mv.visitLabel(l0);
            }
        }
    }
}
