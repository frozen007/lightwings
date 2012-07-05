package org.lightwings.sqlrabbit.log;

public class LogManager {

    private static SQLLogger logger = null;
    private static SQLLoggerFactory factory = null;

    static {
        factory = new DefaultSQLLoggerFactory();
        logger = factory.createSQLLogger();
    }

    public static void setSQLLoggerFactory(SQLLoggerFactory factory) {
        LogManager.factory = factory;
        logger = LogManager.factory.createSQLLogger();
    }

    public static SQLLogger getSQLLogger() {
        return LogManager.logger;
    }
}
