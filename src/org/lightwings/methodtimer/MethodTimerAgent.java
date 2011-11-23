package org.lightwings.methodtimer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.HashSet;

public class MethodTimerAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        HashMap<String, HashSet<String>> def = null;
        try {
            def = getDefinitionFile(agentArgs);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        inst.addTransformer(new ExecutionRecordTransformer(def));
    }

    public static HashMap<String, HashSet<String>> getDefinitionFile(String file) {
        HashMap<String, HashSet<String>> def = new HashMap<String, HashSet<String>>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            String currentClassName = null;
            while (line != null) {
                line = reader.readLine();
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.equals("")) {
                    continue;
                }
                if (line.startsWith("[")) {
                    currentClassName = line.substring(1, line.lastIndexOf(']'));
                    def.put(currentClassName, new HashSet<String>());
                } else {
                    if (currentClassName != null) {
                        HashSet<String> methodSet = def.get(currentClassName);
                        methodSet.add(line);
                    }
                }
            }
        } catch (Exception e) {

        }

        return def;
    }
}
