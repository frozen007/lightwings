package org.lightwings.logger;

/**
 * LogAppender�����
 *
 */
public interface LogRepository {
    public LogAppender getLogAppender(LogEvent e);

    public LogAppender createLogAppender(LogEvent e);

    public void resetAppenders();
}
