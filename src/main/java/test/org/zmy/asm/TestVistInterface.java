package test.org.zmy.asm;

import java.sql.Connection;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class TestVistInterface extends ClassAdapter implements Opcodes {

    public TestVistInterface(ClassVisitor cv) {
        super(cv);
    }

    public static void main(String[] arg) throws Exception {
        ClassReader cr = new ClassReader(Connection.class.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TestVistInterface tvi = new TestVistInterface(cw);
        cr.accept(tvi, 0);
        ImplClassLoader loader = new ImplClassLoader();
        byte[] classbyte = cw.toByteArray();
        Class c = loader.load("org.zmy.asm.ConnectionImpl", classbyte);
        System.out.println(c);
        Connection o = (Connection) c.newInstance();
        o.close();

        AdviceAdapter aa;
    }

    public void visit(int version,
        int access,
        String name,
        String signature,
        String superName,
        String[] interfaces) {
        String[] newInterfaces = new String[]{"java/sql/Connection"};
        super
            .visit(version, ACC_PUBLIC, "org/zmy/asm/ConnectionImpl", signature, superName, newInterfaces);
    }

    public void visitSource(String source,
        String debug) {

        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);

        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        super.visitSource(source, debug);
    }

    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}
