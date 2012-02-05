package org.lightwings.sqlrabbit;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.sql.DriverManager;

public class SqlRabbitAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        DriverManagerInnovator innovator = new DriverManagerInnovator();
        byte[] classBytes = innovator.innovate("java/sql/DriverManager");
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
