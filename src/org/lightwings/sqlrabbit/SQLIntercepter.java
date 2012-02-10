package org.lightwings.sqlrabbit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import org.lightwings.sqlrabbit.log.LogManager;
import org.lightwings.sqlrabbit.log.SQLLogger;

public class SQLIntercepter {

    private static HashSet<String> interceptSet = new HashSet<String>();
    private static SQLLogger logger = LogManager.getSQLLogger();

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
            logger.err(e);
        }
    }

    public static boolean isIntercept(String sql) {
        String upperSql = sql.toUpperCase();
        if (upperSql.indexOf("UPDATE") != -1) {
            if (upperSql.indexOf("ACCOUNT") != -1 || upperSql.indexOf("STKLIST") != -1) {
                return true;
            }
        }
        return false;
    }

    /*
    public static boolean isIntercept(String sql) {
        for (String interceptStr : interceptSet) {
            if (sql.indexOf(interceptStr) != -1) {
                return true;
            }
        }
        return false;
    }*/
}
