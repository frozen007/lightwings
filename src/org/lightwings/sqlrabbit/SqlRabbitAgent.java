package org.lightwings.sqlrabbit;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.sql.DriverManager;

import org.lightwings.asm.ASMMethodInfo;
import org.lightwings.asm.MethodEntry;
import org.lightwings.asm.MethodInjector;
import org.lightwings.asm.RegistrableMethodDedicator;
import org.objectweb.asm.Opcodes;

public class SqlRabbitAgent implements Opcodes {

    public static void premain(String agentArgs, Instrumentation inst) {
        new SqlRabbitAgent().doPremainForApp(agentArgs, inst);
    }

    public void doPremain(String agentArgs, Instrumentation inst) {
        RegistrableMethodDedicator mDedicator = new RegistrableMethodDedicator();
        ASMMethodInfo methodInfo =
            new ASMMethodInfo(ACC_PUBLIC + ACC_STATIC, "getConnection", null, null);
        MethodEntry entry = new MethodEntry(methodInfo, new ConnectionProxyMethodFactory());
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

    public void doPremainForApp(String agentArgs, Instrumentation inst) {
        System.out.println("doPremainForApp Begin");
        RegistrableMethodDedicator mDedicator = new RegistrableMethodDedicator();
        ASMMethodInfo methodInfo =
            new ASMMethodInfo(ACC_PUBLIC, "getConnection", null, null);
        MethodEntry entry = new MethodEntry(methodInfo, new ConnectionProxyMethodFactory());
        mDedicator.registerMethod(entry);
        MethodInjector injector = new MethodInjector(mDedicator);

        String className="com.stock.framework.database.BasicConnectionPool";
        Class target = null;
        try {
            target = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            return;
        }
        byte[] classBytes = injector.innovate(className.replace('.', '/'));
        ClassDefinition classDef = new ClassDefinition(target, classBytes);
        try {
            inst.redefineClasses(classDef);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
        System.out.println("doPremainForApp End");
    }
}
