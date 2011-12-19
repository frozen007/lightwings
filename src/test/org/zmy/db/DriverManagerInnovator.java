package test.org.zmy.db;

import org.lightwings.asm.ASMClassInnovator;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DriverManagerInnovator extends ASMClassInnovator {

    @Override
    protected byte[] transformClass(String className, ClassReader reader, ClassWriter writer) {
        DriverManagerVisitor cv = new DriverManagerVisitor(writer);
        reader.accept(cv, ClassReader.SKIP_DEBUG);
        return writer.toByteArray();
    }

    protected boolean isInnovate(String className) {
        if("java/sql/DriverManager".equals(className)) {
            return true;
        }
        return false;
    }

    class DriverManagerVisitor extends ClassAdapter implements Opcodes {

        public DriverManagerVisitor(ClassVisitor cv) {
            super(cv);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            if (access == (ACC_PUBLIC + ACC_STATIC) && "getConnection".equals(name)) {
                mv = new GetConnectionMethodVisitor(mv);
                System.out.println("method:"+name);
            }
            return mv;
        }
    }

    class GetConnectionMethodVisitor extends MethodAdapter implements Opcodes {

        public GetConnectionMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitInsn(int opcode) {
            /*
            if (opcode == ARETURN) {
                mv.visitInsn(DUP);
                mv.visitVarInsn(ALOAD, 1);               
            }
            */
            mv.visitInsn(opcode);
        }

    }
}
