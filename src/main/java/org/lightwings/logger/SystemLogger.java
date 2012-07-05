package org.lightwings.logger;


public class SystemLogger {
    private DailyFileLogService serv = null;

    /*
     * 创建系统日志服务
     * path 日志文件基础路径
     */
    protected SystemLogger(String path) {
        initializeLogService(path);
    }

    /*
     * 初始化日志服务    
     */
    private void initializeLogService(String path) {
        serv = new DailyFileLogService(path);

        serv.start();

    }

    /**
     * 输出日志
     * @param logName 日志名
     * @param logContent 日志内容
     */
    public void log(String logName, String logContent) {
        //根据日志名和日志内容创建LogEvent，并分发给日志服务线程
        LogEvent logE = new LogEvent(logName, logContent);
        serv.dispatch(logE);
    }
}
