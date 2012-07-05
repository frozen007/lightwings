package org.lightwings.logger;

/**
 * ��־����
 *
 */
public class LogEvent {

    //��־��
    private String logName;

    //��־����
    private String logContent;

    /**
     * ������־����
     * @param logName ��־��
     * @param logContent ��־����
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
