package test.org.zmy.db;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class TestDBOperationClassVisitor extends ClassAdapter implements Opcodes {

    private String targetClass = null;
    private ClassLoader loader = null;

    public TestDBOperationClassVisitor(ClassLoader loader, ClassVisitor cv) {
        super(cv);
        this.loader = loader;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        System.out.print("visit class name=" + name+",signature="+signature+":");
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                System.out.print(interfaces[i]+",");
            }
        }
        System.out.println();
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("getDBValue")) {
            mv = new DBOperationMethodVisitor(mv);
        }
        return mv;
    }

    class DBOperationMethodVisitor extends MethodAdapter implements Opcodes {

        public DBOperationMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == ARETURN) {
                mv.visitInsn(DUP);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(PUTFIELD, targetClass, "sql", "Ljava/lang/String;");
            }
            mv.visitInsn(opcode);
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            mv.visitMethodInsn(opcode, owner, name, desc);
        }
    }

}
