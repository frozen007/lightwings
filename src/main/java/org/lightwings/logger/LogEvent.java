package org.lightwings.logger;

/**
 * 日志对象
 *
 */
public class LogEvent {

    //日志名
    private String logName;

    //日志内容
    private String logContent;

    /**
     * 创建日志对象
     * @param logName 日志名
     * @param logContent 日志内容
     */
    public LogEvent(String logName, String logContent) {
        this.logName = logName;
        this.logContent = logContent;
    }

    public String getLogName() {
        return logName;
    }

    public String getLogContent() {
        return logContent;
    }

}
