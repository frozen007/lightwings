package org.lightwings.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SystemAnalyzeLogger
 * ϵͳ��־������
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
     * ��ӡ��־
     * @param logName ��־��
     * @param logContent ��־����
     */
    public static void print(String logName, String logContent) {
        if (logger == null) {
            logger = SystemAnalyzeLogger.getInstance();
        }
        //����־��������־
        //�����ʽ��<2011-11-23 10:30:01.254>XXXXXXXXX
        logger.log(logName, sdf.format(new Date()) + logContent);
    }
}
