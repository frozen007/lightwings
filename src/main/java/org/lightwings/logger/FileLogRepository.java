package org.lightwings.logger;

import java.io.File;

/**
 * FileLogRepository
 * 用于管理FileLogAppender
 * 
 */
public class FileLogRepository extends DefaultLogRepository {
    protected File logPath = null;

    public FileLogRepository(String path) {
        logPath = new File(path);
        if (!logPath.exists()) {
            logPath.mkdirs();
        } else {
            if (logPath.isFile()) {
                logPath = logPath.getParentFile();
            }
        }
    }

    @Override
    public LogAppender createLogAppender(LogEvent e) {
        File file = getFile(e);
        return new FileLogAppender(file, true);
    }

    public File getFile(LogEvent e) {
        return new File(logPath, e.getLogName() + ".log");
    }
}
