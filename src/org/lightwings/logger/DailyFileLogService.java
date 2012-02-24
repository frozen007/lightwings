package org.lightwings.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * 基于当天日期的日志服务
 * 每天会按yyyyMMdd为后缀名生成新的日志文件
 */
public class DailyFileLogService extends AbstractLogService {
    private static final String TIME_PATTERN = "yyyyMMdd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
    private long lastDate = -1;

    /**
     * 用指定的basePath-日志文件根路径创建日志服务
     * @param basePath 日志文件根路径
     */
    public DailyFileLogService(String basePath) {
        super(new RollingFileLogRepository(basePath, TIME_PATTERN));
        Calendar cal = Calendar.getInstance();
        lastDate = Long.parseLong(sdf.format(cal.getTime()));
    }

    public void process(Collection<LogEvent> list) {
        //检测跨天的情况，关闭前一天的日志，并生成新的日志文件
        checkClosePreviousDateLogRepository();

        super.process(list);
    }

    /*
     * 跨天检查
     */
    protected void checkClosePreviousDateLogRepository() {
        Calendar cal = Calendar.getInstance();
        long currentDate = Long.parseLong(sdf.format(cal.getTime()));
        if (currentDate <= lastDate) {
            return;
        }

        //已跨天，重置所有appender，关闭文件
        super.repository.resetAppenders();
    }
}
