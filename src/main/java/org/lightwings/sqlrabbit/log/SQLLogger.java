package org.lightwings.sqlrabbit.log;

public interface SQLLogger {

    public void log(String sql);

    public void err(Throwable e);
}
