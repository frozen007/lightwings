package org.lightwings.logger;

import java.util.HashMap;

/**
 * DefaultLogRepository
 * 
 * LogAppender缓存库基本实现
 * 以LogEvent的logName为key存储LogAppender
 * 
 */
public class DefaultLogRepository implements LogRepository {
    private HashMap<String, LogAppender> appenderMap = new HashMap<String, LogAppender>();

    /**
     * 根据LogEvent查找对应的LogAppender
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
                    //以logName为key，缓存LogAppender
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
     * 重置所有LogAppender
     * 关闭LogAppender并清空缓存
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
