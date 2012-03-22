package test.org.zmy.asm;

import java.lang.reflect.Constructor;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TestClassGod extends ClassAdapter implements Opcodes {

    public TestClassGod(ClassVisitor cv) {
        super(cv);
    }

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        ClassReader cr = new ClassReader(FooInterface.class.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TestClassGod god = new TestClassGod(cw);
        cr.accept(god, 0);
        ImplClassLoader loader = new ImplClassLoader();
        byte[] classbyte = cw.toByteArray();
        Class c = loader.load("test.org.zmy.asm.FooImpl", classbyte);
        System.out.println(c);
        Constructor con = c.getConstructor(Bar.class);
        FooInterface fi = (FooInterface) con.newInstance(new Bar());
        fi.doFoo(100);
        
    }


    public void visit(int version,
        int access,
        String name,
        String signature,
        String superName,
        String[] interfaces) {
        String[] newInterfaces = new String[]{"test/org/zmy/asm/FooInterface"};
        super
            .visit(version, ACC_PUBLIC, "test/org/zmy/asm/FooImpl", signature, superName, newInterfaces);
    }

    public void visitSource(String source,
        String debug) {

        FieldVisitor fv = cv.visitField(ACC_PUBLIC, "bar", "Ltest/org/zmy/asm/Bar;", null, null);
        fv.visitEnd();

        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>", "(Ltest/org/zmy/asm/Bar;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, "test/org/zmy/asm/FooImpl", "bar", "Ltest/org/zmy/asm/Bar;");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        super.visitSource(source, debug);
    }

    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "test/org/zmy/asm/FooImpl", "bar", "Ltest/org/zmy/asm/Bar;");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "test/org/zmy/asm/Bar", "doBar", "(I)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        return mv;
    }
}
