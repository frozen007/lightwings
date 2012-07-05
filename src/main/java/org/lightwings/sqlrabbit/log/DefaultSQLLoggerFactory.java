package org.lightwings.sqlrabbit.log;

public class DefaultSQLLoggerFactory implements SQLLoggerFactory {

    public SQLLogger createSQLLogger() {
        return new SystemConsoleSQLLogger();
    }

}
