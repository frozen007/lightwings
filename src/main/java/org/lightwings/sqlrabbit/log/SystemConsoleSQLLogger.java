package org.lightwings.sqlrabbit.log;

public class SystemConsoleSQLLogger implements SQLLogger {

    public void log(String sql) {
        System.out.println(sql);
    }

    public void err(Throwable e) {
        e.printStackTrace();
    }

}
