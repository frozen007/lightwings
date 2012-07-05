package org.lightwings.methodtimer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lightwings.asm.ClassInnovator;
import org.lightwings.sqlrabbit.JDBCStatementInnovator;

public class MethodTimerAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        doAgent(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        doAgent(agentArgs, inst);
    }

    public static void doAgent(String agentArgs, Instrumentation inst) {
        HashMap<String, HashSet<String>> def = null;
        try {
            def = getDefinitionFile(agentArgs);
        } catch (Exception e) {
        }

        ExecutionRecordInnovator innovator = new ExecutionRecordInnovator(def);
        ArrayList<ClassDefinition> classDefList = new ArrayList<ClassDefinition>();
        for (String className : def.keySet()) {
            try {
                byte[] classBytes = innovator.innovate(className);
                if (classBytes == null) {
                    continue;
                }

                ClassDefinition classDef = new ClassDefinition(Class.forName(className.replace('/', '.')), classBytes);
                classDefList.add(classDef);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HashMap<String, ClassInnovator> innovatePlan = new HashMap<String, ClassInnovator>();
        String statClass = "oracle/jdbc/driver/OraclePreparedStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));
        statClass = "oracle/jdbc/driver/T4CPreparedStatement";
        innovatePlan.put(statClass, new JDBCStatementInnovator(statClass));

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

    public static HashMap<String, HashSet<String>> getDefinitionFile(String file) {
        HashMap<String, HashSet<String>> def = new HashMap<String, HashSet<String>>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            String currentClassName = null;
            line = reader.readLine();
            while (line != null) {

                if (line.startsWith("[")) {
                    currentClassName = line.substring(1, line.lastIndexOf(']'));
                    def.put(currentClassName, new HashSet<String>());
                } else if (line.startsWith("#") || line.equals("")) {
                    // ignore
                } else {
                    if (currentClassName != null) {
                        HashSet<String> methodSet = def.get(currentClassName);
                        methodSet.add(line);
                    }
                }
                line = reader.readLine();
            }
        } catch (Exception e) {

        }

        return def;
    }
}
