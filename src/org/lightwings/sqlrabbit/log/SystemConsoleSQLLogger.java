package org.lightwings.sqlrabbit.log;

public class SystemConsoleSQLLogger implements SQLLogger {

    @Override
    public void log(String sql) {
        System.out.println(sql);
    }

}
