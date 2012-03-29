package test.org.zmy.db;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.sql.DriverManager;

import org.lightwings.asm.ASMMethodInfo;
import org.lightwings.asm.MethodEntry;
import org.lightwings.asm.MethodInjector;
import org.lightwings.asm.MethodVisitorFactory;
import org.lightwings.asm.RegistrableMethodDedicator;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TestDBOperationAgent implements Opcodes {

    public static void premain(String agentArgs, Instrumentation inst) {
        doPremain(agentArgs, inst);
    }

    public static void doPremain(String agentArgs, Instrumentation inst) {
        RegistrableMethodDedicator mDedicator = new RegistrableMethodDedicator();
        ASMMethodInfo methodInfo =
            new ASMMethodInfo(ACC_PUBLIC + ACC_STATIC, "getConnection", null, null);
        MethodEntry entry = new MethodEntry(methodInfo, new MethodVisitorFactory(){

            @Override
            public MethodVisitor createMethodVisitor(MethodVisitor mv) {
                return new MethodAdapter(mv){
                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == ARETURN) {
                            mv.visitMethodInsn(
                                INVOKESTATIC,
                                "test/org/zmy/db/TestConnectionMock",
                                "createConnectionProxy",
                                "(Ljava/sql/Connection;)Ljava/sql/Connection;");
                        }

                        mv.visitInsn(opcode);
                    }
                };
            }});

        mDedicator.registerMethod(entry);
        MethodInjector injector = new MethodInjector(mDedicator);

        byte[] classBytes = injector.innovate("java/sql/DriverManager");
        ClassDefinition classDef = new ClassDefinition(DriverManager.class, classBytes);
        try {
            inst.redefineClasses(classDef);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }
}
