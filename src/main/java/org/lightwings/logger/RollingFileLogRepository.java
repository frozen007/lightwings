package org.lightwings.logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RollingFileLogRepository
 * 用于管理带时间戳的日志文件
 */
public class RollingFileLogRepository extends FileLogRepository {
    private SimpleDateFormat sdf = null;

    RollingFileLogRepository(String path) {
        this(path, null);
    }

    public RollingFileLogRepository(String path, String timePattern) {
        super(path);
        if (timePattern != null) {
            sdf = new SimpleDateFormat(timePattern);
        } else {
            sdf = new SimpleDateFormat("yyyyMMdd");
        }
    }

    @Override
    public File getFile(LogEvent e) {
        return new File(logPath, e.getLogName() + "_" + sdf.format(new Date()) + ".log");
    }
}
