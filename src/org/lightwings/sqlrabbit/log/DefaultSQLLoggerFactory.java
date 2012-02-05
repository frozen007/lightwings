package org.lightwings.sqlrabbit.log;

public class DefaultSQLLoggerFactory implements SQLLoggerFactory {

    @Override
    public SQLLogger createSQLLogger() {
        return new SystemConsoleSQLLogger();
    }

}
