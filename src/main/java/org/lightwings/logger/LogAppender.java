package org.lightwings.logger;

/**
 * LogAppender
 *
 * ����������־���
 */
public interface LogAppender {

    public void append(String log);

    public void close();
}
