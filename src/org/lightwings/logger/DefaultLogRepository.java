package org.lightwings.logger;

import java.util.HashMap;

/**
 * DefaultLogRepository
 * 
 * LogAppender��������ʵ��
 * ��LogEvent��logNameΪkey�洢LogAppender
 * 
 */
public class DefaultLogRepository implements LogRepository {
    private HashMap<String, LogAppender> appenderMap = new HashMap<String, LogAppender>();

    /**
     * ����LogEvent���Ҷ�Ӧ��LogAppender
     */
    public LogAppender getLogAppender(LogEvent e) {
        String logName = e.getLogName();
        LogAppender appender = null;
        appender = appenderMap.get(logName);
        if (appender == null) {
            synchronized (appenderMap) {
                appender = appenderMap.get(logName);
                if (appender == null) {
                    appender = createLogAppender(e);
                    //��logNameΪkey������LogAppender
                    appenderMap.put(logName, appender);
                }
            }
        }

        return appender;
    }

    public LogAppender createLogAppender(LogEvent e) {
        return new FileLogAppender(e.getLogName() + ".log", true);
    }

    /**
     * ��������LogAppender
     * �ر�LogAppender����ջ���
     */
    public void resetAppenders() {
        synchronized (appenderMap) {
            for (LogAppender appender : appenderMap.values()) {
                appender.close();
            }
            appenderMap.clear();
        }
    }
}
