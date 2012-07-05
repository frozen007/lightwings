package org.lightwings.logger;

/**
 * 日志过滤器接口
 * 
 */
public interface LogFilter {

    /**
     * 检查对应的LogEvent是否可以输出日志
     * @param e LogEvent
     * @return true-输出日志，false-不输出日志
     */
    boolean doFilter(LogEvent e);
}
