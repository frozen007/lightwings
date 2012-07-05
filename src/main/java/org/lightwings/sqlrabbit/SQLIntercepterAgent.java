package org.lightwings.sqlrabbit;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lightwings.asm.ClassInnovator;

public class SQLIntercepterAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        HashMap<String, ClassInnovator> innovatePlan = new HashMap<String, ClassInnovator>();

        //ora9i
        String statClass = "oracle/jdbc/driver/OraclePreparedStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));
        statClass = "oracle/jdbc/driver/OracleStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));

        //ora10g
        statClass = "oracle/jdbc/driver/T4CPreparedStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));
        statClass = "oracle/jdbc/driver/T4CStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));


        ArrayList<ClassDefinition> classDefList = new ArrayList<ClassDefinition>();
        for (String className : innovatePlan.keySet()) {
            ClassInnovator transformer1 = innovatePlan.get(className);
            byte[] classBytes = getClassBytes(className);
            if (classBytes != null) {
                classBytes = transformer1.innovate(className);
                try {
                    ClassDefinition classDef = new ClassDefinition(Class.forName(className.replace('/', '.')),
                                    classBytes);
                    classDefList.add(classDef);
                } catch (Exception e) {

                }
            }
        }

        if (!classDefList.isEmpty()) {
            ClassDefinition[] classDefArr = new ClassDefinition[classDefList.size()];
            try {
                inst.redefineClasses(classDefList.toArray(classDefArr));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getClassBytes(String className) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            InputStream is = ClassLoader.getSystemResourceAsStream(className + ".class");
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                bs.write(buf, 0, len);
            }
            bytes = bs.toByteArray();
        } catch (Exception e) {

        }
        return bytes;
    }

}
