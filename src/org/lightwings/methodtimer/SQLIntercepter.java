package org.lightwings.methodtimer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class SQLIntercepter {

    private static HashSet<String> interceptSet = new HashSet<String>();

    static {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sqlintercept.properties"));
            String line = reader.readLine();
            while (line != null) {
                if (!"".equals(line)) {
                    interceptSet.add(line);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isIntercept(String sql) {
        for (String interceptStr : interceptSet) {
            if (sql.indexOf(interceptStr) != -1) {
                return true;
            }
        }
        return false;
    }
}
