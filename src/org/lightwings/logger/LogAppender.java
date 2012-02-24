package org.lightwings.logger;

/**
 * LogAppender
 *
 * 负责具体的日志输出
 */
public interface LogAppender {

    public void append(String log);

    public void close();
}
