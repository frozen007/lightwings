package test.org.zmy.db;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.lightwings.asm.ClassInnovator;
import org.lightwings.sqlrabbit.DriverManagerInnovator;

public class DBValueAgent implements ClassFileTransformer {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new DBValueAgent(), true);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        /*
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TestDBOperationClassVisitor visitor = new TestDBOperationClassVisitor(loader, writer);

        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        */
        ClassInnovator innovator = new DriverManagerInnovator();

        return innovator.innovate(className);
    }
}
