package org.lightwings.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SystemAnalyzeLogger
 * 系统日志服务类
 */
public class SystemAnalyzeLogger extends SystemLogger {
    private static SystemAnalyzeLogger logger = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("<yyyy-MM-dd HH:mm:ss.SSS>");

    private SystemAnalyzeLogger(String path) {
        super(path);
    }

    public static SystemAnalyzeLogger getInstance() {
        if (logger == null) {
            synchronized (SystemAnalyzeLogger.class) {
                if (logger == null) {
                    logger = new SystemAnalyzeLogger("analyzelog");
                }
            }
        }
        return logger;
    }

    /**
     * 打印日志
     * @param logName 日志名
     * @param logContent 日志内容
     */
    public static void print(String logName, String logContent) {
        if (logger == null) {
            logger = SystemAnalyzeLogger.getInstance();
        }
        //按日志名生成日志
        //输出样式：<2011-11-23 10:30:01.254>XXXXXXXXX
        logger.log(logName, sdf.format(new Date()) + logContent);
    }
}
